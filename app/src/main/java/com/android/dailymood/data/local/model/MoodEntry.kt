package com.android.dailymood.data.local.model;

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val date: String, // formato: yyyy-MM-dd
    val moodScale: Int, // 1 a 5
    val emoji: String,
    val description: String?
)
