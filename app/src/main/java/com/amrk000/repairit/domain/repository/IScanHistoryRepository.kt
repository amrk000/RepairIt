package com.amrk000.repairit.domain.repository

import com.amrk000.repairit.data.model.HistoryItem

interface IScanHistoryRepository {
    companion object{
        val MAX_RECORDS = 100
    }

    suspend fun getData(): List<HistoryItem>

    suspend fun countData(): Long

    suspend fun addRecord(tableRecord: HistoryItem): Long

    suspend fun deleteOldestRecord(): Int
}