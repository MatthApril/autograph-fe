
package com.example.autograph_fe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import com.example.autograph_fe.ui.theme.AutographTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.autograph_fe.ui.navigation.AutographNavHost

class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels { AppViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val savedDarkTheme by appViewModel.darkTheme.collectAsStateWithLifecycle()
            val hasSeenOnboarding by appViewModel.hasSeenOnboarding.collectAsStateWithLifecycle()
            val isDark = savedDarkTheme ?: isSystemInDarkTheme()   // no choice saved yet: follow the phone

            AutographTheme(darkTheme = isDark) {
                AutographNavHost(
                    hasSeenOnboarding = hasSeenOnboarding,
                    isDarkTheme = isDark,
                    onToggleTheme = { appViewModel.setDarkTheme(!isDark) },
                )
            }
        }
    }
}
