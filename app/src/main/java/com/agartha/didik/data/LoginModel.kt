package com.agartha.didik.data

/**
 * Model data untuk menampung informasi login user.
 */
data class LoginModel(
    val username: String, // Nama pengguna atau ID unik user
    val password: String  // Kata sandi untuk autentikasi
)
