package com.example.autograph_fe.auth

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TokenManager(context: Context) {

    private val secureStorage = SecureTokenStorage(
        context.applicationContext
    )

    @Volatile
    private var accessToken: String? = null

    fun getAccessToken(): String? {
        return accessToken
    }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) = withContext(Dispatchers.IO) {
        secureStorage.saveRefreshToken(refreshToken)
        this@TokenManager.accessToken = accessToken
    }

    suspend fun getRefreshToken(): String? =
        withContext(Dispatchers.IO) {
            secureStorage.getRefreshToken()
        }

    suspend fun clearTokens() =
        withContext(Dispatchers.IO) {
            accessToken = null
            secureStorage.clearRefreshToken()
        }
}