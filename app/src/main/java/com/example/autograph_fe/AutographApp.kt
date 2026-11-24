package com.example.autograph_fe

import android.app.Application
import com.example.autograph_fe.auth.SecureTokenStorage
import com.example.autograph_fe.auth.TokenManager
import com.example.autograph_fe.data.preferences.UserPreferences
import com.example.autograph_fe.data.remote.ApiErrorParser
import com.example.autograph_fe.data.remote.ApiService
import com.example.autograph_fe.data.remote.RetrofitClient
import com.example.autograph_fe.data.repository.auth.AuthRepository
import com.example.autograph_fe.data.repository.auth.FakeAuthRepository
import com.example.autograph_fe.data.repository.auth.RemoteAuthRepository

class AutographApp : Application() {

    val userPreferences by lazy { UserPreferences(this) }
    val secureTokenStorage by lazy { SecureTokenStorage(this) }
    val tokenManager by lazy { TokenManager(secureTokenStorage) }

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
