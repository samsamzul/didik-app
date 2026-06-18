package com.agartha.didik.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agartha.didik.data.local.entity.UserEntity
import com.agartha.didik.data.repository.UserRepository
import kotlinx.coroutines.launch

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<UserEntity?>()
    val userProfile: LiveData<UserEntity?> = _userProfile

    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> = _updateStatus

    fun getUserProfile(userId: Int) {
        viewModelScope.launch {
            val profile = userRepository.getUserProfile(userId)
            _userProfile.value = profile
        }
    }

    fun updateProfile(userId: Int, name: String, email: String) {
        viewModelScope.launch {
            val currentUser = userRepository.getUserProfile(userId)
            if (currentUser != null) {
                val updatedUser = currentUser.copy(
                    namaLengkap = name,
                    email = email
                )
                userRepository.updateProfile(updatedUser)
                _updateStatus.value = true
            } else {
                _updateStatus.value = false
            }
        }
    }
}