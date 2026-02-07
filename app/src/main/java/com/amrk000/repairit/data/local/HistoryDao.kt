package com.amrk000.repairit.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amrk000.repairit.data.model.HistoryItem

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY DATETIME(date, 'UTC') DESC")
    fun getData(): List<HistoryItem>

    @Query("SELECT COUNT(*) FROM history")
    fun countData(): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRecord(tableRecord: HistoryItem): Long

    @Query("DELETE FROM history WHERE id = (SELECT MIN(id) FROM history)")
    fun deleteOldestRecord(): Int
}