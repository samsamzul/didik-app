package com.agartha.didik.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.agartha.didik.data.local.entity.UserEntity

@Dao
interface UserDao {
    // 2.4.1 Registrasi Akun Baru
    @Insert
    suspend fun insertUser(user: UserEntity): Long

    // 2.4.2 Login (Ambil user berdasarkan email unik)
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    // 2.4.7 Mengelola Akun (Update Profil)
    @Update
    suspend fun updateUserFields(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?
}