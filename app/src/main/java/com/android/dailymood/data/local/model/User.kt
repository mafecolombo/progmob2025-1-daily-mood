package com.android.dailymood.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user") // Nome da Tabela na DB
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val photoUri: String? = null

)
