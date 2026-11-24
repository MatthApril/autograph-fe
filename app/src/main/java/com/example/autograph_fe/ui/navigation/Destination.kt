package com.example.autograph_fe.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable data object Welcome : Destination
    @Serializable data object Login : Destination
    @Serializable data object Register : Destination
    @Serializable data object Onboarding : Destination
    @Serializable data object Home : Destination

    /** Register passes the email and the server's timings to Verify email. */
    @Serializable
    data class VerifyEmail(
        val email: String,
        val expiresInSeconds: Int,
        val resendInSeconds: Int,
        val needsNewCode: Boolean,
    ) : Destination

    /** Verify email passes the new user's name to Verified. */
    @Serializable
    data class Verified(val name: String) : Destination
}
