package com.android.dailymood.utils

object EmailValidator {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

    fun isValid(email: String): Boolean {
        return email.matches(emailRegex)
    }
}
