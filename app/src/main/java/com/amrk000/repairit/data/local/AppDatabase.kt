package com.amrk000.repairit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amrk000.repairit.data.model.HistoryItem

@Database(
    entities = [HistoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): HistoryDao
}