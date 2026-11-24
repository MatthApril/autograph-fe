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
import com.example.autograph_fe.data.repository.auth.AuthRepository
import com.example.autograph_fe.ui.auth.GoogleAuthManager
import com.example.autograph_fe.ui.util.UiText
import com.example.autograph_fe.ui.util.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
)

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                emailError = null,
                errorMessage = null
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = null,
                errorMessage = null
            )
        }
    }

    fun logIn(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return

        val emailError = AuthValidator.email(state.email)
        val passwordError = AuthValidator.password(state.password)

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            authRepository.logIn(state.email.trim(), state.password)
                .onSuccess {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toUiText()
                        )
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
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as AutographApp
                LoginViewModel(app.authRepository)
            }
        }
    }
}