package com.example.autograph_fe.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class TokenManager(
    private val secureTokenStorage: SecureTokenStorage
) {

    private val _accessToken = MutableStateFlow<String?>(null)

    val accessToken = _accessToken.asStateFlow()

    fun getAccessToken(): String? {
        return _accessToken.value
    }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        // Simpan refresh token terlebih dahulu
        withContext(Dispatchers.IO) {
            secureTokenStorage.saveRefreshToken(refreshToken)
        }

        // Simpan access token di RAM
        _accessToken.value = accessToken
    }

    suspend fun getRefreshToken(): String? {
        return withContext(Dispatchers.IO) {
            secureTokenStorage.getRefreshToken()
        }
    }

    suspend fun updateAccessToken(
        accessToken: String
    ) {
        _accessToken.value = accessToken
    }

    suspend fun clear() {
        _accessToken.value = null

        withContext(Dispatchers.IO) {
            secureTokenStorage.clearRefreshToken()
        }
    }
}