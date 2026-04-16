package com.agartha.didik.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel untuk mengelola data review secara aman (Lifecycle-aware).
 * Bertugas menyediakan data untuk UI dan menangani penambahan data baru.
 */
class ReviewViewModel : ViewModel() {

    // Menyimpan daftar review secara internal (Private) agar tidak bisa diubah langsung dari luar
    private val _reviews = MutableLiveData<MutableList<ReviewModel>>(
        ReviewRepository.getAllReviews().toMutableList()
    )
    
    // Ekspos data sebagai LiveData (Read-only) agar UI bisa memantau perubahannya (Observasi)
    val reviews: LiveData<MutableList<ReviewModel>> = _reviews

    /**
     * Menambahkan review baru ke Repository dan memperbarui tampilan UI.
     */
    fun addReview(newReview: ReviewModel) {
        // 1. Simpan ke Repository (Sumber data utama)
        ReviewRepository.addReview(newReview)
        
        // 2. Update nilai LiveData agar UI yang melakukan observasi otomatis diperbarui
        val currentList = _reviews.value ?: mutableListOf()
        currentList.add(newReview)
        _reviews.value = currentList
    }
}
