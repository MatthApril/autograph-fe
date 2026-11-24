package com.example.autograph_fe.ui.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.autograph_fe.R
import com.example.autograph_fe.data.repository.AuthError

/** A message that's either our own string resource or plain text from the server. */
sealed interface UiText {
    data class Plain(val value: String) : UiText
    data class Resource(@StringRes val id: Int, val args: List<Any> = emptyList()) : UiText
}

@Composable
fun UiText.asString(): String = when (this) {
    is UiText.Plain -> value
    is UiText.Resource -> stringResource(id, *args.toTypedArray())
}

/** The general message for an error. Screens override cases they show differently. */
fun Throwable.toUiText(): UiText = when (this) {
    is AuthError.Network -> UiText.Resource(R.string.error_network)
    is AuthError.InvalidCredentials -> UiText.Resource(R.string.error_invalid_credentials)
    is AuthError.EmailTaken -> UiText.Resource(R.string.error_email_taken)
    is AuthError.Unknown -> message?.let { UiText.Plain(it) } ?: UiText.Resource(R.string.error_generic)
    else -> UiText.Resource(R.string.error_generic)
}
