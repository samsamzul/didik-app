package com.agartha.didik.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agartha.didik.data.local.entity.UserEntity
import com.agartha.didik.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    // Logika Registrasi Akun Baru
    fun register(nama: String, email: String, pass: String, instansi: String) {
        _authState.value = AuthState.Loading

        // Validasi aturan bisnis: Harus menggunakan email instansi (.sch)
        if (!email.endsWith(".ac.id")) {
            _authState.value = AuthState.Error("Registrasi gagal: Harus menggunakan email instansi (.ac.id)")
            return
        }

        viewModelScope.launch {
            try {
                val newUser = UserEntity(namaLengkap = nama, email = email, password = pass, namaInstansi = instansi)
                userRepository.registerUser(newUser)
                _authState.postValue(AuthState.Success("Registrasi Berhasil! Silakan Login."))
            } catch (e: Exception) {
                _authState.postValue(AuthState.Error("Terjadi kesalahan database: ${e.message}"))
            }
        }
    }

    // Logika Pencocokan Data Login
    fun login(email: String, pass: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val user = userRepository.loginUser(email)
                if (user != null && user.password == pass) {
                    _authState.postValue(AuthState.LoginSuccess(user.id, user.namaLengkap))
                } else {
                    _authState.postValue(AuthState.Error("Email atau Password salah!"))
                }
            } catch (e: Exception) {
                _authState.postValue(AuthState.Error("Koneksi database error: ${e.message}"))
            }
        }
    }
}

// Sealed class pembungkus status kiriman ke Activity UI
sealed class AuthState {
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class LoginSuccess(val userId: Int, val userName: String) : AuthState()
    data class Error(val error: String) : AuthState()
}