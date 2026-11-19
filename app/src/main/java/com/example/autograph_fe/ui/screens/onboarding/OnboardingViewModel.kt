package com.example.autograph_fe.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.autograph_fe.AutographApp
import com.example.autograph_fe.data.preferences.UserPreferences
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    fun finishOnboarding(onSaved: () -> Unit) {
        viewModelScope.launch {
            userPreferences.setHasSeenOnboarding()
            onSaved()   // navigate only AFTER the save finished
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AutographApp
                OnboardingViewModel(app.userPreferences)
            }
        }
    }
}