package com.example.autograph_fe.ui.screens.auth

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.autograph_fe.AutographApp
import com.example.autograph_fe.data.remote.ApiService
import com.example.autograph_fe.data.repository.auth.AuthError
import com.example.autograph_fe.data.repository.auth.AuthRepository
import com.example.autograph_fe.models.PendingVerification
import com.example.autograph_fe.ui.auth.GoogleAuthManager
import com.example.autograph_fe.ui.util.UiText
import com.example.autograph_fe.ui.util.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    @StringRes val nameError: Int? = null,
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null,
    @StringRes val confirmPasswordError: Int? = null,
    val acceptedTerms: Boolean = false,
    val termsError: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
) {
    val passwordStrength: Int get() = AuthValidator.passwordStrength(password)
}

class RegisterViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) =
        _uiState.update { it.copy(name = value, nameError = null, errorMessage = null) }

    fun onEmailChange(value: String) =
        _uiState.update { it.copy(email = value, emailError = null, errorMessage = null) }

    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, passwordError = null, errorMessage = null) }

    fun onConfirmPasswordChange(value: String) =
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null, errorMessage = null) }

    fun onAcceptedTermsChange(value: Boolean) =
        _uiState.update { it.copy(acceptedTerms = value, termsError = false) }

    fun register(onVerificationNeeded: (PendingVerification) -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return

        val nameError = AuthValidator.name(state.name)
        val emailError = AuthValidator.email(state.email)
        val passwordError = AuthValidator.newPassword(state.password)
        val confirmError = AuthValidator.confirmPassword(state.password, state.confirmPassword)
        val termsMissing = !state.acceptedTerms

        if (listOf(nameError, emailError, passwordError, confirmError).any { it != null } || termsMissing) {
            _uiState.update {
                it.copy(
                    nameError = nameError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmError,
                    termsError = termsMissing,
                )
            }
            return
        }

        val email = state.email.trim().lowercase()     // the server lowercases too

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            authRepository.register(state.name.trim(), email, state.password)
                .onSuccess { timing ->
                    _uiState.update { it.copy(isLoading = false) }
                    onVerificationNeeded(
                        PendingVerification(
                            email = timing.email,
                            expiresInSeconds = timing.expiresInSeconds,
                            resendInSeconds = timing.resendInSeconds,
                            needsNewCode = false,
                        )
                    )
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    if (error is AuthError.VerificationPending) {
                        // A sign-up for this email is already waiting: verify it instead of failing
                        onVerificationNeeded(
                            PendingVerification(
                                email = email,
                                expiresInSeconds = CODE_LIFETIME_SECONDS,
                                resendInSeconds = CODE_LIFETIME_SECONDS,
                                needsNewCode = true,
                            )
                        )
                    } else {
                        _uiState.update { it.copy(errorMessage = error.toUiText()) }
                    }
                }
        }
    }

    fun signInWithGoogle(
        googleAuthManager: GoogleAuthManager,
        webClientId: String,
        onSuccess: () -> Unit
    ) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val idToken = googleAuthManager.getGoogleCreds(
                    webClientId = webClientId
                )

                // TODO: POST /api/v1/auth/google
                // authRepository.loginWithGoogle(idToken)
                Log.d("GoogleAuth", "Google ID Token: $idToken")
                Log.d("GoogleAuth", "Google Sign-In successful")

                _uiState.update {
                    it.copy(isLoading = false)
                }

                ApiService.auth.signInWithGoogleService(idToken)
                // Panggil setelah autentikasi backend berhasil.
                // onSuccess()

            } catch (e: Exception) {
                Log.e("GoogleAuth", "Google Sign-In failed", e)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = UiText.Plain(e.message ?: "Google Sign-In failed.")
                    )
                }
            }
        }
    }

    companion object {
        const val CODE_LIFETIME_SECONDS = 60   // matches OTP_TTL_SECONDS on the server

        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as AutographApp
                RegisterViewModel(app.authRepository)
            }
        }
    }
}
