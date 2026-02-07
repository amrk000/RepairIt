package com.amrk000.repairit.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.amrk000.repairit.R
import com.amrk000.repairit.data.local.HistoryDao
import com.amrk000.repairit.data.model.HistoryItem
import com.amrk000.repairit.data.remote.GeminiService
import com.amrk000.repairit.domain.repository.IGeminiRepository
import com.amrk000.repairit.domain.repository.IScanHistoryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GeminiRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val GeminiService: GeminiService
) : IGeminiRepository {

    val prompt = """
        check if this item is broken or degraded and if so act as a technician and provide the following:
        Output strict format in ### [dont change section names]:
        -Name: item name (3 words max)
        -Brand: item brand (2 words max)
        -Model: item model (3 words max)
        -Main Specs: item main specs (max 5 in brief separated by comma)
        -Problem: what is the detected problem if found
        -Tools: the need tools i need to fix (separated by comma)
        -Fix: steps how to fix it my self (3 to 9 points)
    """

    override suspend fun diagnoseItem(image: Bitmap): String?{
        val currentLanguage = context.getString(R.string.language)

        return GeminiService.processPrompt(
            prompt.replace("###", currentLanguage),
            image
        )
    }

}