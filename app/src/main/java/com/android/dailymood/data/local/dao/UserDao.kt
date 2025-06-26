package com.android.dailymood.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.dailymood.data.local.model.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): User?

    @Update
    suspend fun updateUser(user: User)


}