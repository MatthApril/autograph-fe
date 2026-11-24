package com.example.autograph_fe.ui.screens.verify

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autograph_fe.R
import com.example.autograph_fe.models.User
import com.example.autograph_fe.ui.components.AutographButton
import com.example.autograph_fe.ui.components.AutographDialog
import com.example.autograph_fe.ui.components.FormErrorBanner
import com.example.autograph_fe.ui.components.HaloIcon
import com.example.autograph_fe.ui.components.OTP_LENGTH
import com.example.autograph_fe.ui.components.OtpCodeInput
import com.example.autograph_fe.ui.components.TextLink
import com.example.autograph_fe.ui.screens.auth.AuthTopBar
import com.example.autograph_fe.ui.theme.AutographTheme
import com.example.autograph_fe.ui.util.UiText
import com.example.autograph_fe.ui.util.asString
import com.example.autograph_fe.ui.util.formatCountdown

@Composable
fun VerifyEmailRoute(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onVerified: (User) -> Unit,
    onBackToRegister: () -> Unit,
    viewModel: VerifyEmailViewModel = viewModel(factory = VerifyEmailViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // The system back button and back gesture ask first, just like the arrow
    BackHandler(enabled = !uiState.showLeaveDialog) { viewModel.onBackRequested() }

    VerifyEmailScreen(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        onCodeChange = viewModel::onCodeChange,
        onVerify = { viewModel.verify(onVerified) },
        onResend = { viewModel.resendCode() },
        onChangeEmail = onBackToRegister,
        onBack = viewModel::onBackRequested,
        onLeaveConfirmed = onBackToRegister,
        onLeaveDialogDismissed = viewModel::onLeaveDialogDismissed,
        onStartOver = onBackToRegister,
        onToastShown = viewModel::onToastShown,
    )
}

@Composable
fun VerifyEmailScreen(
    uiState: VerifyEmailUiState,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onCodeChange: (String) -> Unit,
    onVerify: () -> Unit,
    onResend: () -> Unit,
    onChangeEmail: () -> Unit,
    onBack: () -> Unit,
    onLeaveConfirmed: () -> Unit,
    onLeaveDialogDismissed: () -> Unit,
    onStartOver: () -> Unit,
    onToastShown: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Show the toast once, then tell the ViewModel it was shown
    val toastText = uiState.toast?.asString()
    LaunchedEffect(toastText) {
        if (toastText != null) {
            snackbarHostState.showSnackbar(toastText)
            onToastShown()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding(),
        ) {
            AuthTopBar(
                title = stringResource(R.string.verify_top_title),
                onBack = onBack,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(12.dp))
                HaloIcon(iconRes = R.drawable.ic_mail, iconTint = colors.primary)
                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.verify_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.verify_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = uiState.email,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(2.dp))
                TextLink(text = stringResource(R.string.verify_change_email), onClick = onChangeEmail)
                Spacer(Modifier.height(12.dp))

                OtpCodeInput(
                    code = uiState.code,
                    onCodeChange = onCodeChange,
                    isError = uiState.codeError != null,
                    enabled = !uiState.isExpired,
                    onComplete = { onVerify() },           // the 6th digit submits
                    focusRequester = focusRequester,
                )
                Spacer(Modifier.height(10.dp))
                StatusLine(uiState)
                Spacer(Modifier.height(12.dp))

                if (uiState.bannerError != null) {
                    FormErrorBanner(message = uiState.bannerError.asString())
                    Spacer(Modifier.height(12.dp))
                }

                if (uiState.isExpired) {
                    AutographButton(
                        text = stringResource(R.string.otp_send_new_code),
                        onClick = onResend,
                        modifier = Modifier.fillMaxWidth(),
                        isLoading = uiState.isResending,
                    )
                } else {
                    AutographButton(
                        text = stringResource(R.string.verify_button),
                        onClick = onVerify,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.code.length == OTP_LENGTH,
                        isLoading = uiState.isVerifying,
                    )
                }
                Spacer(Modifier.height(12.dp))
                ResendLine(uiState = uiState, onResend = onResend)
                Spacer(Modifier.height(24.dp))
            }

            // Pinned to the bottom, outside the scrolling content
            Text(
                text = stringResource(R.string.otp_spam_hint),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, lineHeight = 15.sp),
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 12.dp),
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .systemBarsPadding()
                .imePadding()
                .padding(16.dp),
        ) { data ->
            Snackbar(
                snackbarData = data,
                shape = RoundedCornerShape(50),
                containerColor = colors.onBackground,   // dark pill in light mode, light pill in dark
                contentColor = colors.background,
            )
        }
    }

    // Focus the boxes and open the keyboard when the screen opens, and again when a new code
    // makes the boxes usable after expiring. Placed after the layout so the field exists.
    LaunchedEffect(uiState.isExpired) {
        if (!uiState.isExpired) {
            focusRequester.requestFocus()
            keyboard?.show()
        }
    }

    if (uiState.showLeaveDialog) {
        AutographDialog(
            title = stringResource(R.string.leave_title),
            message = stringResource(R.string.leave_message),
            primaryText = stringResource(R.string.leave_stay),
            onPrimaryClick = onLeaveDialogDismissed,
            onDismissRequest = onLeaveDialogDismissed,
            iconRes = R.drawable.ic_lock,
            secondaryText = stringResource(R.string.leave_leave),
            onSecondaryClick = onLeaveConfirmed,
        )
    }

    uiState.timedOutMessage?.let { message ->
        AutographDialog(
            title = stringResource(R.string.timed_out_title),
            message = message.asString(),
            primaryText = stringResource(R.string.start_over),
            onPrimaryClick = onStartOver,
            onDismissRequest = onStartOver,      // nothing else is possible, so closing = starting over
            iconRes = R.drawable.ic_mail,
        )
    }
}

/** The line under the boxes: countdown, wrong code, or expired. */
@Composable
private fun StatusLine(uiState: VerifyEmailUiState) {
    val colors = MaterialTheme.colorScheme
    val style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)

    when {
        uiState.codeError != null ->
            Text(uiState.codeError.asString(), style = style, color = colors.error)

        uiState.isExpired ->
            Text(stringResource(R.string.otp_expired), style = style, color = colors.error)

        else -> {
            val prefix = stringResource(R.string.otp_expires_in)
            Text(
                text = buildAnnotatedString {
                    append(prefix)
                    append(" ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(formatCountdown(uiState.secondsLeft))
                    }
                },
                style = style,
                color = colors.onSurfaceVariant,
            )
        }
    }
}

/** "Didn't get it? Resend in 0:42" → "Didn't get it? Resend code". */
@Composable
private fun ResendLine(uiState: VerifyEmailUiState, onResend: () -> Unit) {
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.otp_didnt_get_it),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
            color = muted,
        )
        Spacer(Modifier.width(4.dp))
        when {
            uiState.isResending -> Text(
                text = stringResource(R.string.otp_sending),
                style = MaterialTheme.typography.labelMedium,
                color = muted,
            )
            uiState.resendInSeconds > 0 -> Text(
                text = stringResource(R.string.otp_resend_in, formatCountdown(uiState.resendInSeconds)),
                style = MaterialTheme.typography.labelMedium,
                color = muted,
            )
            else -> TextLink(text = stringResource(R.string.otp_resend), onClick = onResend)
        }
    }
}

// Previews: one per state, numbered in the mockup's order

@Composable
private fun VerifyPreview(state: VerifyEmailUiState) = AutographTheme {
    VerifyEmailScreen(
        uiState = state, isDarkTheme = isSystemInDarkTheme(), onToggleTheme = {},
        onCodeChange = {}, onVerify = {}, onResend = {}, onChangeEmail = {}, onBack = {},
        onLeaveConfirmed = {}, onLeaveDialogDismissed = {}, onStartOver = {}, onToastShown = {},
    )
}

private val previewState = VerifyEmailUiState(email = "maya@example.com", secondsLeft = 57, resendInSeconds = 57)

@Preview(name = "1 Empty", showBackground = true, heightDp = 844)
@Composable private fun EmptyPreview() = VerifyPreview(previewState)

@Preview(name = "2 Typing", showBackground = true, heightDp = 844)
@Composable private fun TypingPreview() = VerifyPreview(previewState.copy(code = "482"))

@Preview(name = "3 Verifying", showBackground = true, heightDp = 844)
@Composable private fun VerifyingPreview() = VerifyPreview(previewState.copy(code = "482917", isVerifying = true))

@Preview(name = "4 Wrong code", showBackground = true, heightDp = 844)
@Composable private fun WrongPreview() = VerifyPreview(
    previewState.copy(code = "111111", codeError = UiText.Resource(R.string.otp_error_invalid))
)

@Preview(name = "5 Expired", showBackground = true, heightDp = 844)
@Composable private fun ExpiredPreview() = VerifyPreview(previewState.copy(secondsLeft = 0, resendInSeconds = 0))

@Preview(name = "6 Network error", showBackground = true, heightDp = 844, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable private fun NetworkPreview() = VerifyPreview(
    previewState.copy(code = "000000", bannerError = UiText.Resource(R.string.error_network))
)

@Preview(name = "7 Leave dialog", showBackground = true, heightDp = 844)
@Composable private fun LeavePreview() = VerifyPreview(previewState.copy(showLeaveDialog = true))
