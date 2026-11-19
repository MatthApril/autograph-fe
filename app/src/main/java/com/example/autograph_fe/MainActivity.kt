
package com.example.autograph_fe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.autograph_fe.ui.auth.GoogleAuthManager
import com.example.autograph_fe.ui.screens.welcome.WelcomeScreen
import com.example.autograph_fe.ui.theme.AutographTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var googleAuthManager: GoogleAuthManager

    private fun loginWithGoogle() {
        lifecycleScope.launch {
            try {
                val idToken = googleAuthManager.getGoogleCreds(
                    webClientId = getString(R.string.google_web_client_id)
                )

                // POST /api/v1/auth/google
                Log.d("GoogleAuth", "Google Sign-In successful")

            } catch (e: Exception) {
                Log.e(
                    "GoogleAuth",
                    "Google Sign-In failed",
                    e
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        googleAuthManager = GoogleAuthManager(this)

        setContent {
            val systemDark = isSystemInDarkTheme()
            var isDark by rememberSaveable {
                mutableStateOf(systemDark)
            }

            AutographTheme(darkTheme = isDark) {
                WelcomeScreen(
                    isDarkTheme = isDark,
                    onToggleTheme = { isDark = !isDark },
                    onCreateAccount = { loginWithGoogle() },
                    onLogIn = { },
                    onContinueAsGuest = { },
                )
            }
        }
    }
}
