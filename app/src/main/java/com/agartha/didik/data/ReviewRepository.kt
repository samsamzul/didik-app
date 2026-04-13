package com.agartha.didik.data

object ReviewRepository {
    private val reviewList = mutableListOf<ReviewModel>()

    init {
        reviewList.add(
            ReviewModel(
                1,
                "PT. Artajasa",
                "Data Analyst",
                "Mengerjakan query SQL dan pembersihan data harian.", // Ini jobDesc
                "Lingkungannya asik banget buat belajar!",
                4.8f
            )
        )
    }

    fun getAllReviews(): List<ReviewModel> = reviewList

    fun addReview(review: ReviewModel) {
        reviewList.add(review)
    }
}