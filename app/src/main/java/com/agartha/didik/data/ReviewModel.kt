package com.agartha.didik.data

/**
 * Model data utama untuk menampung satu objek review pekerjaan.
 */
data class ReviewModel(
    val id: Int,              // ID unik untuk setiap review
    val companyName: String,  // Nama perusahaan yang direview
    val position: String,     // Posisi/Jabatan pekerjaan
    val jobDesc: String,      // Deskripsi pekerjaan
    val reviewerName: String, // Nama orang yang memberikan review
    val reviewText: String,   // Isi teks review
    val rating: Float         // Skor bintang yang diberikan (1.0 - 5.0)
)
