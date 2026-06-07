package com.agartha.didik.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agartha.didik.data.Injection
import com.agartha.didik.data.repository.UserRepository
import com.agartha.didik.ui.auth.AuthViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(userRepository) as T
            }
            // Besok kalau ada ReviewViewModel atau CompanyViewModel, tinggal kamu tambah di bawah sini:
            // modelClass.isAssignableFrom(ReviewViewModel::class.java) -> { ... }
            else -> throw IllegalArgumentException("ViewModel Tidak Dikenal: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        // Fungsi Singleton biar pemanggilan Factory seragam di seluruh aplikasi Didik
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                // Sediakan UserRepository-mu di sini (pastikan Injection/provideRepository sudah kamu buat ya, Nad)
                val repository = Injection.provideRepository(context)
                instance ?: ViewModelFactory(repository)
            }.also { instance = it }
        }
    }
}