package com.example.autograph_fe.ui.screens.verify

import android.os.SystemClock
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.autograph_fe.AutographApp
import com.example.autograph_fe.R
import com.example.autograph_fe.data.repository.AuthError
import com.example.autograph_fe.data.repository.AuthRepository
import com.example.autograph_fe.models.User
import com.example.autograph_fe.ui.components.OTP_LENGTH
import com.example.autograph_fe.ui.navigation.Destination
import com.example.autograph_fe.ui.util.UiText
import com.example.autograph_fe.ui.util.toUiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VerifyEmailUiState(
    val email: String,
    val code: String = "",
    val secondsLeft: Int = 0,              // until the code expires
    val resendInSeconds: Int = 0,          // until "Resend code" unlocks
    val isVerifying: Boolean = false,
    val isResending: Boolean = false,
    val codeError: UiText? = null,         // under the boxes; turns them red
    val bannerError: UiText? = null,       // red banner: network or server trouble
    val showLeaveDialog: Boolean = false,
    val timedOutMessage: UiText? = null,   // not null = show the "Start over" dialog
    val toast: UiText? = null,             // shown once, then cleared
) {
    val isExpired: Boolean get() = secondsLeft == 0
    val canVerify: Boolean get() = code.length == OTP_LENGTH && !isVerifying && !isExpired
    val canResend: Boolean get() = resendInSeconds == 0 && !isResending && !isVerifying
}

class VerifyEmailViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val clock: () -> Long = SystemClock::elapsedRealtime,
) : ViewModel() {

    // The arguments Register passed when it navigated here
    private val route = savedStateHandle.toRoute<Destination.VerifyEmail>()

    private val _uiState = MutableStateFlow(VerifyEmailUiState(email = route.email))
    val uiState: StateFlow<VerifyEmailUiState> = _uiState.asStateFlow()

    private var expiresAt = 0L      // clock time when the code expires
    private var resendAt = 0L       // clock time when resending unlocks
    private var countdownJob: Job? = null

    init {
        startCountdown(route.expiresInSeconds, route.resendInSeconds)
        if (route.needsNewCode) resendCode(force = true)
    }

    // Events from the screen

    fun onCodeChange(value: String) {
        _uiState.update { it.copy(code = value, codeError = null, bannerError = null) }
    }

    fun verify(onVerified: (User) -> Unit) {
        val state = _uiState.value
        if (!state.canVerify) return

        viewModelScope.launch {
            _uiState.update { it.copy(isVerifying = true, codeError = null, bannerError = null) }
            authRepository.verifyEmail(state.email, state.code)
                .onSuccess { user ->
                    countdownJob?.cancel()
                    _uiState.update { it.copy(isVerifying = false) }
                    onVerified(user)
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isVerifying = false) }
                    handleError(error)
                }
        }
    }

    fun resendCode(force: Boolean = false) {
        val state = _uiState.value
        if (state.isResending || (!force && !state.canResend)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isResending = true, codeError = null, bannerError = null) }
            authRepository.resendCode(state.email)
                .onSuccess { timing ->
                    _uiState.update {
                        it.copy(
                            isResending = false,
                            code = "",
                            toast = UiText.Resource(R.string.otp_toast_sent, listOf(timing.email)),
                        )
                    }
                    startCountdown(timing.expiresInSeconds, timing.resendInSeconds)
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isResending = false) }
                    handleError(error)
                }
        }
    }

    fun onToastShown() = _uiState.update { it.copy(toast = null) }
    fun onBackRequested() = _uiState.update { it.copy(showLeaveDialog = true) }
    fun onLeaveDialogDismissed() = _uiState.update { it.copy(showLeaveDialog = false) }

    // Errors

    private fun handleError(error: Throwable) {
        when (error) {
            is AuthError.InvalidCode ->
                _uiState.update { it.copy(codeError = UiText.Resource(R.string.otp_error_invalid)) }

            is AuthError.CodeExpired ->
                // The server says it's expired: make the timer agree
                startCountdown(expiresInSeconds = 0, resendInSeconds = secondsUntil(resendAt))

            is AuthError.ResendTooSoon ->
                // Codes live exactly as long as the resend cooldown (60 s), so both end together
                startCountdown(expiresInSeconds = error.waitSeconds, resendInSeconds = error.waitSeconds)

            is AuthError.SignUpTimedOut ->
                _uiState.update { it.copy(timedOutMessage = UiText.Resource(R.string.otp_timed_out_message)) }

            is AuthError.TooManyAttempts ->
                _uiState.update { it.copy(timedOutMessage = UiText.Resource(R.string.otp_too_many_attempts_message)) }

            else ->
                _uiState.update { it.copy(bannerError = error.toUiText()) }
        }
    }

    // Countdown: stores deadlines and recomputes from the real clock, so it never drifts
    // and keeps counting while the app is in the background.

    private fun startCountdown(expiresInSeconds: Int, resendInSeconds: Int) {
        val now = clock()
        expiresAt = now + expiresInSeconds * 1_000L
        resendAt = now + resendInSeconds * 1_000L

        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            do {
                val secondsLeft = secondsUntil(expiresAt)
                val resendIn = secondsUntil(resendAt)
                _uiState.update { state ->
                    val justExpired = secondsLeft == 0 && state.secondsLeft > 0
                    state.copy(
                        secondsLeft = secondsLeft,
                        resendInSeconds = resendIn,
                        // An expired code can't be used, so clear what was typed
                        code = if (justExpired) "" else state.code,
                        codeError = if (justExpired) null else state.codeError,
                    )
                }
                delay(250)
            } while (secondsLeft > 0 || resendIn > 0)
        }
    }

    /** Whole seconds left until [deadline], rounded down, never negative. */
    private fun secondsUntil(deadline: Long): Int =
        ((deadline - clock()) / 1_000L).toInt().coerceAtLeast(0)

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as AutographApp
                VerifyEmailViewModel(createSavedStateHandle(), app.authRepository)
            }
        }
    }
}
