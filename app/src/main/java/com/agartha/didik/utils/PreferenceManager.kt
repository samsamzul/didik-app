package com.agartha.didik.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Manajer untuk mengelola sesi lokal (SharedPreferences) aplikasi Didik.
 * Berfungsi untuk menyimpan status login, ID user, dan nama user yang aktif.
 */
class PreferenceManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    // 1. Menyimpan status apakah user sudah login atau belum
    fun setLoginStatus(isLoggedIn: Boolean) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    // 2. Mengecek apakah user sedang dalam posisi login (dipakai di SplashActivity)
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // 3. Menyimpan ID User aktif dari database SQLite setelah sukses Login
    fun saveUserId(userId: Int) {
        editor.putInt(KEY_USER_ID, userId)
        editor.apply()
    }

    // 4. Mengambil ID User yang sedang aktif
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    // 5. Menyimpan Nama Lengkap User aktif untuk sapaan di Dashboard
    fun saveUserName(userName: String) {
        editor.putString(KEY_USER_NAME, userName)
        editor.apply()
    }

    // 6. Mengambil Nama Lengkap User yang sedang aktif
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, "")
    }

    // 7. Menghapus sesi / Logout (Menghapus cache login di HP)
    fun clearSession() {
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "didik_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
    }
}