package com.example.myswap.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("myswap_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_NAME = "key_name"
    }

    fun saveName(name: String) {
        prefs.edit().putString(KEY_NAME, name).apply()
    }

    fun getName(): String {
        return prefs.getString(KEY_NAME, "User") ?: "User"
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
