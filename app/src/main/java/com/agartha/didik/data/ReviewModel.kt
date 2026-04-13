package com.agartha.didik.data

data class ReviewModel(
    val id: Int,
    val companyName: String,
    val position: String,
    val jobDesc: String,
    val reviewText: String,
    val rating: Float
)