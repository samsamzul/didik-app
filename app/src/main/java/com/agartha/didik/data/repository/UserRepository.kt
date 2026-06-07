package com.agartha.didik.data.repository

import com.agartha.didik.data.local.dao.UserDao
import com.agartha.didik.data.local.entity.UserEntity

class UserRepository private constructor(private val userDao: UserDao) { // <-- Dibuat private constructor-nya

    // Menyambungkan fungsi Registrasi (2.4.1)
    suspend fun registerUser(user: UserEntity): Long {
        return userDao.insertUser(user)
    }

    // Menyambungkan fungsi Login (2.4.2)
    suspend fun loginUser(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    // Menyambungkan fungsi Mengelola Akun / Update Profil (2.4.7)
    suspend fun updateProfile(user: UserEntity) {
        userDao.updateUserFields(user)
    }

    // Mengambil data profil user aktif berdasarkan ID
    suspend fun getUserProfile(userId: Int): UserEntity? {
        return userDao.getUserById(userId)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        // Fungsi sakti buat nyambungin kabel ke Injection.kt
        fun getInstance(userDao: UserDao): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userDao).also { instance = it }
            }
    }
}