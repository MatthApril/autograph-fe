package com.example.autograph_fe.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable data object Welcome : Destination
    @Serializable data object Login : Destination
    @Serializable data object Register : Destination
    @Serializable data object Onboarding : Destination
    @Serializable data object Home : Destination
}
