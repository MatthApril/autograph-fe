package com.example.autograph_fe.ui.screens.auth

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.autograph_fe.AutographApp
import com.example.autograph_fe.data.repository.AuthRepository
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
    val errorMessage: String? = null,
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

    fun register(onSuccess: () -> Unit) {
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

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            authRepository.register(state.name.trim(), state.email.trim(), state.password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Something went wrong.")
                    }
                }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as AutographApp
                RegisterViewModel(app.authRepository)
            }
        }
    }
}
