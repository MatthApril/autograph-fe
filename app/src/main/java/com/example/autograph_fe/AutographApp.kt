package com.example.autograph_fe

import android.app.Application
import com.example.autograph_fe.auth.TokenManager
import com.example.autograph_fe.data.preferences.UserPreferences
import com.example.autograph_fe.data.remote.ApiErrorParser
import com.example.autograph_fe.data.remote.ApiService
import com.example.autograph_fe.data.remote.RetrofitClient
import com.example.autograph_fe.data.repository.AuthRepository
import com.example.autograph_fe.data.repository.FakeAuthRepository
import com.example.autograph_fe.data.repository.RemoteAuthRepository

class AutographApp : Application() {

    val userPreferences by lazy { UserPreferences(this) }
    val tokenManager by lazy { TokenManager(this) }

    val authRepository: AuthRepository by lazy {
        if (USE_FAKE_BACKEND) {
            FakeAuthRepository()
        } else {
            RemoteAuthRepository(
                service = ApiService.auth,
                tokenManager = tokenManager,
                errorParser = ApiErrorParser(RetrofitClient.moshi),
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        RetrofitClient.initialize(this)
    }

    private companion object {
        /** Flip to true to work on screens without running the server. */
        const val USE_FAKE_BACKEND = false
    }
}
