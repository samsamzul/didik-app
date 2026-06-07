package com.agartha.didik.data

import android.content.Context
import com.agartha.didik.data.local.DidikDatabase
import com.agartha.didik.data.repository.UserRepository

object Injection {

    fun provideRepository(context: Context): UserRepository {
        // Disamakan memanggil .getDatabase(context) sesuai file DidikDatabase-mu!
        val database = DidikDatabase.getDatabase(context)

        val userDao = database.userDao()

        // Suntikkan ke UserRepository
        return UserRepository.getInstance(userDao)
    }
}