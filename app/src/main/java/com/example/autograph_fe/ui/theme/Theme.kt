package com.example.autograph_fe.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Indigo,           onPrimary = Color.White,
    background = Paper,         onBackground = Ink,
    surface = Color.White,      onSurface = Ink,
    onSurfaceVariant = InkMuted,
    outline = PaperLine,
)

private val DarkColors = darkColorScheme(
    primary = IndigoLight,      onPrimary = Night,
    background = Night,         onBackground = Moon,
    surface = NightSurface,     onSurface = Moon,
    onSurfaceVariant = MoonMuted,
    outline = NightLine,
)

@Composable
fun AutographTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AutographTypography,
        content = content,
    )
}