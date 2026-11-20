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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun RegisterRoute(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onRegistered: () -> Unit,
    onBack: () -> Unit,
    onLogIn: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val googleAuthManager = remember(context) {
        GoogleAuthManager(context as Activity)
    }

    val webClientId = stringResource(R.string.google_web_client_id)

    RegisterScreen(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onAcceptedTermsChange = viewModel::onAcceptedTermsChange,
        onRegisterClick = { viewModel.register(onSuccess = onRegistered) },
        onGoogleClick = {
            viewModel.signInWithGoogle(
                googleAuthManager = googleAuthManager,
                webClientId = webClientId,
                onSuccess = onRegistered
            )
        },
        onBack = onBack,
        onLogIn = onLogIn,
    )
}

@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onAcceptedTermsChange: (Boolean) -> Unit,
    onRegisterClick: () -> Unit,
    onBack: () -> Unit,
    onLogIn: () -> Unit,
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
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
            title = stringResource(R.string.register_top_title),
            onBack = onBack,
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.register_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.register_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))

            AutographTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = stringResource(R.string.name),
                placeholder = stringResource(R.string.placeholder_name),
                errorMessage = uiState.nameError?.let { stringResource(it) },
                enabled = !uiState.isLoading,
            )
            Spacer(Modifier.height(14.dp))
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
                placeholder = stringResource(R.string.placeholder_new_password),
                errorMessage = uiState.passwordError?.let { stringResource(it) },
                isPassword = true,
                enabled = !uiState.isLoading,
                belowField = { PasswordStrengthMeter(strength = uiState.passwordStrength) },
            )
            Spacer(Modifier.height(14.dp))
            AutographTextField(
                value = uiState.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = stringResource(R.string.confirm_password),
                placeholder = stringResource(R.string.placeholder_confirm_password),
                errorMessage = uiState.confirmPasswordError?.let { stringResource(it) },
                isPassword = true,
                imeAction = ImeAction.Done,
                onSubmit = onRegisterClick,
                enabled = !uiState.isLoading,
            )
            Spacer(Modifier.height(14.dp))

            TermsCheckbox(
                checked = uiState.acceptedTerms,
                onCheckedChange = onAcceptedTermsChange,
                onTermsClick = onTermsClick,
                onPrivacyClick = onPrivacyClick,
                isError = uiState.termsError,
            )
            if (uiState.termsError) {
                Text(
                    text = stringResource(R.string.error_terms),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 30.dp, top = 4.dp),   // lines up under the sentence
                )
            }

            if (uiState.errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                FormErrorBanner(message = uiState.errorMessage)
            }

            Spacer(Modifier.height(16.dp))
            AutographButton(
                text = stringResource(R.string.create_account),
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth(),
                isLoading = uiState.isLoading,
            )
            Spacer(Modifier.height(16.dp))
            LabeledDivider(text = stringResource(R.string.or))
            Spacer(Modifier.height(12.dp))
            GoogleSignInButton(onClick = onGoogleClick)
            Spacer(Modifier.height(16.dp))
            AuthFooter(
                prompt = stringResource(R.string.have_account_prompt),
                action = stringResource(R.string.log_in),
                onAction = onLogIn,
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

// Filled in like the mockup, so you can compare them side by side
@Preview(name = "Light", showBackground = true, heightDp = 900)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, heightDp = 900)
@Composable
private fun RegisterScreenPreview() {
    AutographTheme {
        RegisterScreen(
            uiState = RegisterUiState(
                name = "Maya Chen",
                email = "maya@example.com",
                password = "notes2026",
                confirmPassword = "notes2026",
                acceptedTerms = true,
            ),
            isDarkTheme = isSystemInDarkTheme(),
            onToggleTheme = {}, onNameChange = {}, onEmailChange = {}, onPasswordChange = {},
            onConfirmPasswordChange = {}, onAcceptedTermsChange = {}, onRegisterClick = {},
            onBack = {}, onLogIn = {},
        )
    }
}
