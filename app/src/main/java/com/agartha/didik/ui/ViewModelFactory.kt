package com.agartha.didik.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agartha.didik.data.Injection
import com.agartha.didik.data.repository.UserRepository
import com.agartha.didik.ui.auth.AuthViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val reviewRepository: com.agartha.didik.data.repository.ReviewRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(com.agartha.didik.ui.review.ReviewViewModel::class.java) -> {
                com.agartha.didik.ui.review.ReviewViewModel(reviewRepository) as T
            }
            modelClass.isAssignableFrom(com.agartha.didik.ui.profile.EditProfileViewModel::class.java) -> {
                com.agartha.didik.ui.profile.EditProfileViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("ViewModel Tidak Dikenal: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                val userRepository = Injection.provideRepository(context)
                val reviewRepository = Injection.provideReviewRepository(context)
                instance ?: ViewModelFactory(userRepository, reviewRepository)
            }.also { instance = it }
        }
    }
}