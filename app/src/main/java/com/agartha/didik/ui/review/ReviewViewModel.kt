package com.agartha.didik.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agartha.didik.data.repository.ReviewRepository
import com.agartha.didik.data.local.entity.UlasanEntity
import kotlinx.coroutines.launch


data class ReviewModel(
    val companyName: String,
    val position: String,
    val category: String,
    val jobDesc: String,
    val reviewText: String,
    val rating: Float,
    val reviewerName: String
)

class ReviewViewModel(private val reviewRepository: ReviewRepository) : ViewModel() {

    private val _reviews = MutableLiveData<List<ReviewModel>>()
    val reviews: LiveData<List<ReviewModel>> get() = _reviews

    // State untuk Multi-step Form (Shared across Fragments)
    var tempCompanyName: String = ""
    var tempPosition: String = ""
    var tempReviewText: String = ""
    var tempRating: Int = 0

    // Ratings spesifik dari Step 1
    var tempRatingWorkload: Int = 0
    var tempRatingMentorship: Int = 0
    var tempRatingCulture: Int = 0

    // Step 2 State
    var tempPro: String = ""
    var tempKontra: String = ""
    var tempSkills: List<String> = listOf()

    init {
        loadReviewsFromDatabase()
    }

    private fun loadReviewsFromDatabase() {
        viewModelScope.launch {
            // Kita akan ambil data asli dari Database via Repository
            // dan memetakan (mapping) ke ReviewModel
            val reviewModels = reviewRepository.getAllReviewsWithDetails()
            _reviews.value = reviewModels
        }
    }

    fun insertReview(ulasan: UlasanEntity) {
        viewModelScope.launch {
            reviewRepository.createNewReview(ulasan)
            loadReviewsFromDatabase() // Refresh data setelah simpan
        }
    }
}
