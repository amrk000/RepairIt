package com.amrk000.repairit.domain.usecase

import com.amrk000.repairit.data.model.HistoryItem
import com.amrk000.repairit.data.repository.HistoryRepository
import com.amrk000.repairit.util.DateTimeUtil
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetScanHistoryUseCase @Inject constructor(val repository: HistoryRepository) {
    suspend operator fun invoke() : List<HistoryItem> {
        val data = repository.getData()

        data.forEach { item ->
            item.date = DateTimeUtil.formatIsoDate(item.date)
        }

        return data
    }
}