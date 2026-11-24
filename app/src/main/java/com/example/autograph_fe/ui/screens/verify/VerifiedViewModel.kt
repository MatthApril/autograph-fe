package com.example.autograph_fe.ui.screens.verify

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.autograph_fe.ui.navigation.Destination

class VerifiedViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    /** "Maya Chen" becomes "Maya"; blank when the server had no name. */
    val firstName: String = savedStateHandle.toRoute<Destination.Verified>()
        .name.trim().substringBefore(" ")

    companion object {
        val Factory = viewModelFactory {
            initializer { VerifiedViewModel(createSavedStateHandle()) }
        }
    }
}
