package com.example.autograph_fe.ui.screens.auth

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autograph_fe.R
import com.example.autograph_fe.ui.auth.GoogleAuthManager
import com.example.autograph_fe.ui.components.AutographButton
import com.example.autograph_fe.ui.components.AutographTextField
import com.example.autograph_fe.ui.components.FormErrorBanner
import com.example.autograph_fe.ui.theme.AutographTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember

@Composable
fun LoginRoute(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onLoggedIn: () -> Unit,
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    viewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.Factory
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val googleAuthManager = remember(context) {
        GoogleAuthManager(context as Activity)
    }

    val webClientId = stringResource(R.string.google_web_client_id)

    LoginScreen(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogInClick = {
            viewModel.logIn(onSuccess = onLoggedIn)
        },
        onGoogleClick = {
            viewModel.signInWithGoogle(
                googleAuthManager = googleAuthManager,
                webClientId = webClientId,
                onSuccess = onLoggedIn
            )
        },
        onBack = onBack,
        onCreateAccount = onCreateAccount,
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogInClick: () -> Unit,
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    onForgotPassword: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding(),
    ) {
        AuthTopBar(
            title = stringResource(R.string.login_top_title),
            onBack = onBack,
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
        )

        // Only the content scrolls; the top bar stays put
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))

            AutographTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.placeholder_email),
                errorMessage = uiState.emailError?.let { stringResource(it) },
                keyboardType = KeyboardType.Email,
                enabled = !uiState.isLoading,
            )
            Spacer(Modifier.height(14.dp))
            AutographTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.placeholder_password),
                errorMessage = uiState.passwordError?.let { stringResource(it) },
                isPassword = true,
                imeAction = ImeAction.Done,
                onSubmit = onLogInClick,
                enabled = !uiState.isLoading,
            )
            TextLink(
                text = stringResource(R.string.forgot_password),
                onClick = onForgotPassword,
                modifier = Modifier.align(Alignment.End).padding(top = 4.dp),
            )

            if (uiState.errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                FormErrorBanner(message = uiState.errorMessage)
            }

            Spacer(Modifier.height(16.dp))
            AutographButton(
                text = stringResource(R.string.log_in),
                onClick = onLogInClick,
                modifier = Modifier.fillMaxWidth(),
                isLoading = uiState.isLoading,
            )
            Spacer(Modifier.height(16.dp))
            LabeledDivider(text = stringResource(R.string.or_continue_with))
            Spacer(Modifier.height(12.dp))
            GoogleSignInButton(onClick = onGoogleClick)
            Spacer(Modifier.height(16.dp))
            AuthFooter(
                prompt = stringResource(R.string.new_here_prompt),
                action = stringResource(R.string.create_account),
                onAction = onCreateAccount,
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenPreview() {
    AutographTheme {
        LoginScreen(
            uiState = LoginUiState(),
            isDarkTheme = isSystemInDarkTheme(),
            onToggleTheme = {}, onEmailChange = {}, onPasswordChange = {},
            onLogInClick = {}, onBack = {}, onCreateAccount = {},
        )
    }
}
