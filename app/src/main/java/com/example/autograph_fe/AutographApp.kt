package com.example.autograph_fe

import android.app.Application
import android.util.Log
import com.example.autograph_fe.data.preferences.UserPreferences
import com.example.autograph_fe.data.remote.RetrofitClient
import com.example.autograph_fe.data.repository.AuthRepository
import com.example.autograph_fe.data.repository.FakeAuthRepository

class AutographApp : Application() {
    val userPreferences by lazy {
        UserPreferences(this)
    }

    val authRepository: AuthRepository by lazy {
        FakeAuthRepository()
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("AutographApp", "Initializing Retrofit")

        try {
            RetrofitClient.initialize(this)

            Log.d("AutographApp", "Retrofit initialized successfully")

        } catch (e: Exception) {
            Log.e("AutographApp", "Retrofit initialization failed", e)
            throw e
        }
    }
}