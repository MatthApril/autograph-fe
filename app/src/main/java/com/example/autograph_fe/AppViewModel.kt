package com.example.autograph_fe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.autograph_fe.data.preferences.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    val darkTheme: StateFlow<Boolean?> = userPreferences.darkTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val hasSeenOnboarding: StateFlow<Boolean> = userPreferences.hasSeenOnboarding
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch { userPreferences.setDarkTheme(isDark) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as AutographApp
                AppViewModel(app.userPreferences)
            }
        }
    }
}