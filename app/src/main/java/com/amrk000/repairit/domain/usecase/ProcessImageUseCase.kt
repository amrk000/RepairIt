package com.amrk000.repairit.domain.usecase

import android.graphics.Bitmap
import com.amrk000.repairit.data.model.HistoryItem
import com.amrk000.repairit.data.model.ResultData
import com.amrk000.repairit.data.repository.GeminiRepository
import com.amrk000.repairit.data.repository.HistoryRepository
import com.amrk000.repairit.util.ImageEncoder
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.time.Instant
import javax.inject.Inject


class ProcessImageUseCase @Inject constructor(
    val GeminiRepository: GeminiRepository,
    val historyRepository: HistoryRepository
)  {

    suspend operator fun invoke(image : Bitmap): MutableStateFlow<ResultData?> {
        val result = MutableStateFlow<ResultData?>(null)

        val response = GeminiRepository.diagnoseItem(image)

        if(response != null){
            result.value = parseResult(response)
        } else result.value = null

        runBlocking {
            if(historyRepository.countData() >= HistoryRepository.MAX_RECORDS){
                historyRepository.deleteOldestRecord()
            }

            if(result.value != null) {
                historyRepository.addRecord(
                    HistoryItem(
                        jsonData = Gson().toJson(result.value),
                        image = ImageEncoder.encode(image),
                        date = Instant.now().toString()
                    )
                )
            }
        }

        return result
    }

    private fun parseResult(result: String): ResultData {

        var input = result.replace("**","")

        fun extractField(key: String): String {
            val regex = Regex("-$key:\\s*(.+)")
            return regex.find(input)?.groupValues?.get(1)?.trim() ?: "Unknown"
        }

        val name = extractField("Name")
        val brand = extractField("Brand")
        val model = extractField("Model")
        val mainSpecs = extractField("Main Specs")
        val problem = extractField("Problem")

        val toolsRaw = extractField("Tools")
        val toolsList = if (toolsRaw != "Unknown") {
            toolsRaw.split(",").map { it.trim() }
        } else emptyList()

        val stepsList = mutableListOf<String>()

        val fixStartIndex = input.indexOf("-Fix:")

        if (fixStartIndex != -1) {
            val fixSection = input.substring(fixStartIndex)
            // Regex to find lines starting with a number and a dot (e.g., "1. ")
            val stepRegex = Regex("^\\d+\\.\\s+(.+)", RegexOption.MULTILINE)

            stepRegex.findAll(fixSection).forEach { matchResult ->
                stepsList.add(matchResult.groupValues[1].trim())
            }
        }

        return ResultData(
            name = name,
            brand = brand,
            model = model,
            mainSpecs = mainSpecs,
            problem = problem,
            tools = toolsList,
            steps = stepsList
        )
    }
}