package com.example.autograph_fe

import android.app.Application
import com.example.autograph_fe.data.preferences.UserPreferences
import com.example.autograph_fe.data.repository.AuthRepository
import com.example.autograph_fe.data.repository.FakeAuthRepository

class AutographApp : Application() {
    val userPreferences by lazy { UserPreferences(this) }
    val authRepository: AuthRepository by lazy { FakeAuthRepository() }
}
