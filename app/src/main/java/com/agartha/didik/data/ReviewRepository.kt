package com.agartha.didik.data

/**
 * Repository sederhana yang bertindak sebagai database sementara (In-Memory).
 * Mengelola daftar review yang ada di dalam aplikasi selama aplikasi berjalan.
 */
object ReviewRepository {
    // List privat untuk menampung data review
    private val reviewList = mutableListOf<ReviewModel>()

    /**
     * Mengambil semua daftar review yang tersimpan.
     */
    fun getAllReviews(): List<ReviewModel> = reviewList

    /**
     * Menambahkan review baru ke dalam daftar.
     */
    fun addReview(review: ReviewModel) {
        reviewList.add(review)
    }
}
