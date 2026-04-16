package com.agartha.didik.data

/**
 * Model data untuk menampung informasi pendaftaran akun baru.
 */
data class RegisterModel(
    val name: String,     // Nama lengkap pendaftar
    val email: String,    // Alamat email pendaftar
    val password: String  // Kata sandi akun baru
)
