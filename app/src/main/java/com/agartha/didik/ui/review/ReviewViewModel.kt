package com.agartha.didik.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agartha.didik.data.repository.ReviewRepository
import com.agartha.didik.data.local.entity.UlasanEntity
import kotlinx.coroutines.launch


data class ReviewModel(
    val reviewId: Int = 0,
    val userId: Int,
    val companyName: String,
    val position: String,
    val category: String,
    val jobDesc: String,
    val reviewText: String,
    val rating: Float,
    val reviewerName: String,
    val ratingWorkload: Int = 0,
    val ratingMentorship: Int = 0,
    val ratingCulture: Int = 0,
    val pros: String = "",
    val cons: String = "",
    val location: String = "Jakarta"
)

class ReviewViewModel(private val reviewRepository: ReviewRepository) : ViewModel() {

    private val _reviews = MutableLiveData<List<ReviewModel>>()
    val reviews: LiveData<List<ReviewModel>> get() = _reviews

    // State untuk Multi-step Form (Shared across Fragments)
    var editingReviewId: Int? = null
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

    fun loadReviewsFromDatabase() {
        viewModelScope.launch {
            val reviewModels = reviewRepository.getAllReviewsWithDetails()
            _reviews.value = reviewModels
        }
    }

    fun insertReview(ulasan: UlasanEntity) {
        viewModelScope.launch {
            reviewRepository.createNewReview(ulasan)
            loadReviewsFromDatabase()
        }
    }

    fun updateReview(ulasan: UlasanEntity) {
        viewModelScope.launch {
            reviewRepository.updateExistingReview(ulasan)
            loadReviewsFromDatabase()
        }
    }

    fun setReviewToEdit(
        id: Int, company: String, pos: String, text: String, 
        rating: Int, work: Int, ment: Int, cult: Int, pros: String, cons: String
    ) {
        editingReviewId = id
        tempCompanyName = company
        tempPosition = pos
        tempReviewText = text
        tempRating = rating
        tempRatingWorkload = work
        tempRatingMentorship = ment
        tempRatingCulture = cult
        tempPro = pros
        tempKontra = cons
    }

    fun clearFormState() {
        editingReviewId = null
        tempCompanyName = ""
        tempPosition = ""
        tempReviewText = ""
        tempRating = 0
        tempRatingWorkload = 0
        tempRatingMentorship = 0
        tempRatingCulture = 0
        tempPro = ""
        tempKontra = ""
        tempSkills = listOf()
    }
}
