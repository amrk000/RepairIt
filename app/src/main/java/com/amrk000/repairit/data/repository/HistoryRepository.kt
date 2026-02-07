package com.amrk000.repairit.data.repository

import android.content.Context
import com.amrk000.repairit.data.local.HistoryDao
import com.amrk000.repairit.data.model.HistoryItem
import com.amrk000.repairit.domain.repository.IScanHistoryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val historyDao: HistoryDao
) : IScanHistoryRepository {
    companion object{
        val MAX_RECORDS = 100
    }

    override suspend fun getData(): List<HistoryItem>{
        return historyDao.getData()
    }

    override suspend fun countData(): Long{
        return historyDao.countData()
    }

    override suspend fun addRecord(tableRecord: HistoryItem): Long{
        return historyDao.addRecord(tableRecord)
    }

    override suspend fun deleteOldestRecord(): Int{
        return historyDao.deleteOldestRecord()
    }
}