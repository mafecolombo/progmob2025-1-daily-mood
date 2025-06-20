package com.android.dailymood.utils
import android.content.Context
import androidx.core.content.edit

object SessionManager {
    fun isFirstTime(context: Context): Boolean {
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("firstTime", true)
    }

    fun setFirstTime(context: Context, value: Boolean) {
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        prefs.edit() { putBoolean("firstTime", false) }
    }

    fun setLoggedInUser(context: Context, userId: Int) {

        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        prefs.edit() { putInt("userId", userId) }
    }

    fun getLoggedInUser(context: Context): Int? {
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("userId", -1)
        return if (userId == -1) null else userId
    }

    fun clearSession(context: Context) {
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        prefs.edit() { clear() }
    }

    }


