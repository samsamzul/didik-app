package com.agartha.didik.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReviewViewModel : ViewModel() {
    // Pastikan namanya 'reviews' bukan yang lain
    private val _reviews = MutableLiveData<MutableList<ReviewModel>>(
        ReviewRepository.getAllReviews().toMutableList()
    )
    val reviews: LiveData<MutableList<ReviewModel>> = _reviews

    fun addReview(newReview: ReviewModel) {
        ReviewRepository.addReview(newReview)
        val currentList = _reviews.value ?: mutableListOf()
        currentList.add(newReview)
        _reviews.value = currentList
    }
}