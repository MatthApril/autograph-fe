package com.example.autograph_fe.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore(name = "session")

/** Keeps the session tokens on the phone so they survive closing the app. */
class TokenManager(private val context: Context) {

    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    val accessToken: Flow<String?> = context.sessionDataStore.data.map { it[Keys.ACCESS_TOKEN] }

    /** Logged in = a refresh token is saved. The access token expires every 15 minutes anyway. */
    val isLoggedIn: Flow<Boolean> = context.sessionDataStore.data.map { it[Keys.REFRESH_TOKEN] != null }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = accessToken
            prefs[Keys.REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun clear() {
        context.sessionDataStore.edit { it.clear() }
    }
}
