package com.example.autograph_fe.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Must be at the top level of the file, outside the class: it creates ONE DataStore for the whole app
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {

    private object Keys {
        val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
    }

    val hasSeenOnboarding: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[Keys.HAS_SEEN_ONBOARDING] ?: false }

    /** null means the user never chose, so follow the phone's setting. */
    val darkTheme: Flow<Boolean?> = context.dataStore.data
        .map { prefs -> prefs[Keys.DARK_THEME] }

    suspend fun setHasSeenOnboarding() {
        context.dataStore.edit { prefs -> prefs[Keys.HAS_SEEN_ONBOARDING] = true }
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.DARK_THEME] = isDark }
    }
}