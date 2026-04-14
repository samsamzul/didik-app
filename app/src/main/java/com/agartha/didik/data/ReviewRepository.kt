package com.agartha.didik.data

object ReviewRepository {
    private val reviewList = mutableListOf<ReviewModel>()

    fun getAllReviews(): List<ReviewModel> = reviewList

    fun addReview(review: ReviewModel) {
        reviewList.add(review)
    }
}
