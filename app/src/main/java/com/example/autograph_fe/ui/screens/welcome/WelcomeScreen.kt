package com.example.autograph_fe.ui.screens.welcome

import android.content.res.Configuration
import android.credentials.CredentialManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autograph_fe.R
import com.example.autograph_fe.ui.components.AutographButton
import com.example.autograph_fe.ui.components.AutographLogo
import com.example.autograph_fe.ui.components.ButtonVariant
import com.example.autograph_fe.ui.components.ThemeToggleButton
import com.example.autograph_fe.ui.components.WelcomeIllustration
import com.example.autograph_fe.ui.theme.AutographTheme

@Composable
fun WelcomeScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onCreateAccount: () -> Unit,
    onLogIn: () -> Unit,
    onContinueAsGuest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()               // keeps content away from the status bar
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ThemeToggleButton(isDark = isDarkTheme, onToggle = onToggleTheme)
        }

        WelcomeIllustration()
        Spacer(Modifier.height(24.dp))
        AutographLogo()
        Spacer(Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.welcome_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.weight(1f))             // pushes the buttons to the bottom

        AutographButton(stringResource(R.string.create_account), onCreateAccount, Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        AutographButton(stringResource(R.string.log_in), onLogIn, Modifier.fillMaxWidth(),
            ButtonVariant.Secondary)
        AutographButton(stringResource(R.string.continue_as_guest), onContinueAsGuest, Modifier.fillMaxWidth(), ButtonVariant.Text)
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WelcomeScreenPreview() {
    AutographTheme {
        WelcomeScreen(isDarkTheme = isSystemInDarkTheme(), onToggleTheme = {}, onCreateAccount = {}, onLogIn = {}, onContinueAsGuest = {})
    }
}