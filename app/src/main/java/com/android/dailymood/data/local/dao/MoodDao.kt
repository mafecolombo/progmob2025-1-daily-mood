package com.android.dailymood.data.local.dao

import androidx.room.*
import com.android.dailymood.data.local.model.MoodEntry

@Dao
interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(entry: MoodEntry)

    @Query("SELECT * FROM mood_entries WHERE userId = :userId ORDER BY date DESC")
    suspend fun getMoodHistory(userId: Int): List<MoodEntry>

    @Query("SELECT * FROM mood_entries WHERE userId = :userId AND date = :date LIMIT 1")
    suspend fun getMoodByDate(userId: Int, date: String): MoodEntry?
}
