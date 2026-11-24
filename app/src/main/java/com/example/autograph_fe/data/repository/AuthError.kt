package com.example.autograph_fe.data.repository

/** Every auth failure the app understands. ViewModels branch on these, never on message text. */
sealed class AuthError : Exception() {
    data class InvalidCode(val attemptsLeft: Int?) : AuthError()
    data object CodeExpired : AuthError()
    data object SignUpTimedOut : AuthError()
    data object TooManyAttempts : AuthError()
    data class ResendTooSoon(val waitSeconds: Int) : AuthError()
    data object VerificationPending : AuthError()
    data object EmailTaken : AuthError()
    data object InvalidCredentials : AuthError()
    data object Network : AuthError()
    data class Unknown(override val message: String?) : AuthError()
}
