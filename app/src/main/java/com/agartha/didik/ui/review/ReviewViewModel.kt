package com.agartha.didik.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// 🌟 STRUKTUR DATA MODEL YANG DICARI ADAPTER & ACTIVITY TEMENMU
data class ReviewModel(
    val companyName: String,
    val position: String,
    val category: String,
    val jobDesc: String,
    val reviewText: String,
    val rating: Float,
    val reviewerName: String
)

class ReviewViewModel : ViewModel() {

    private val _reviews = MutableLiveData<List<ReviewModel>>()
    val reviews: LiveData<List<ReviewModel>> get() = _reviews

    init {
        loadDummyReviews()
    }

    private fun loadDummyReviews() {
        val dummyList = listOf(
            ReviewModel(
                companyName = "Google Indonesia",
                position = "UI/UX Designer Intern",
                category = "Design",
                jobDesc = "Membuat wireframe dan UI design system Skilink.",
                reviewText = "Mentornya sangat suportif dan ramah banget!",
                rating = 4.8f,
                reviewerName = "Nadilah Nur"
            ),
            ReviewModel(
                companyName = "Tokopedia",
                position = "Software Engineer Intern",
                category = "Tech",
                jobDesc = "Maintenance fitur database dan kelola POS.",
                reviewText = "Tech stack-nya bener-bener modern dan asyik.",
                rating = 4.5f,
                reviewerName = "Farhan Alif"
            )
        )
        _reviews.value = dummyList
    }
}