package com.example.eticketing.data

import android.content.Context
import android.content.Intent

object SessionManager {

    private const val PREF_NAME = "session"
    private const val KEY_ROLE = "userRole"
    private const val KEY_NAME = "userName"
    private const val KEY_ID = "userId"
    private const val KEY_CLOSE_TIME = "closeTime"
    private const val ONE_HOUR = 60 * 60 * 1000L // 1 jam dalam milidetik

    fun getUserId(context: Context): Long =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getLong(KEY_ID, -1L)

    fun getUserRole(context: Context): String? =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_ROLE, null)

    fun getUserName(context: Context): String =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_NAME, "Pengguna") ?: "Pengguna"

    fun isLoggedIn(context: Context): Boolean =
        getUserRole(context) != null

    // Dipanggil saat app di-background/close
    fun saveCloseTime(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putLong(KEY_CLOSE_TIME, System.currentTimeMillis())
            .apply()
    }

    // Cek apakah sudah lebih dari 1 jam sejak app di-close
    fun isSessionExpired(context: Context): Boolean {
        val closeTime = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getLong(KEY_CLOSE_TIME, 0L)

        // Jika closeTime = 0 artinya belum pernah di-close, session masih valid
        if (closeTime == 0L) return false

        return System.currentTimeMillis() - closeTime > ONE_HOUR
    }

    // Reset timer close (dipanggil saat app dibuka lagi)
    fun resetCloseTime(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putLong(KEY_CLOSE_TIME, 0L)
            .apply()
    }

    fun logout(context: Context, loginActivityClass: Class<*>) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().clear().apply()
        val intent = Intent(context, loginActivityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}