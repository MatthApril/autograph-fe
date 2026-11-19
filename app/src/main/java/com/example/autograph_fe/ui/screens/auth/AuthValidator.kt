package com.example.autograph_fe.ui.screens.auth

import androidx.annotation.StringRes
import com.example.autograph_fe.R

object AuthValidator {

    private val EMAIL_REGEX = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
    private const val MIN_PASSWORD_LENGTH = 8

    @StringRes
    fun name(value: String): Int? =
        if (value.isBlank()) R.string.error_name_required else null

    @StringRes
    fun email(value: String): Int? = when {
        value.isBlank() -> R.string.error_email_required
        !EMAIL_REGEX.matches(value.trim()) -> R.string.error_email_invalid
        else -> null
    }

    /** For logging in: only checks that something was typed. */
    @StringRes
    fun password(value: String): Int? =
        if (value.isBlank()) R.string.error_password_required else null

    /** For registering: enforces the length rule. */
    @StringRes
    fun newPassword(value: String): Int? = when {
        value.isBlank() -> R.string.error_password_required
        value.length < MIN_PASSWORD_LENGTH -> R.string.error_password_short
        else -> null
    }

    /** 0 = nothing typed, 1 = weak, 2 = good, 3 = strong. */
    fun passwordStrength(value: String): Int {
        if (value.isEmpty()) return 0
        var score = 0
        if (value.length >= MIN_PASSWORD_LENGTH) score++
        if (value.any { it.isDigit() } && value.any { it.isLetter() }) score++
        if (value.length >= 12 && value.any { !it.isLetterOrDigit() }) score++
        return score.coerceAtLeast(1)
    }

    @StringRes
    fun confirmPassword(password: String, confirm: String): Int? =
        if (confirm != password) R.string.error_password_mismatch else null
}
