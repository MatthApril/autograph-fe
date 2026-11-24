package com.example.autograph_fe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Indigo,           onPrimary = Color.White,
    background = Paper,         onBackground = Ink,
    surface = Color.White,      onSurface = Ink,
    onSurfaceVariant = InkMuted,
    outline = PaperLine,
    error = Danger,             errorContainer = DangerSoft,
)

private val DarkColors = darkColorScheme(
    primary = IndigoLight,      onPrimary = Night,
    background = Night,         onBackground = Moon,
    surface = NightSurface,     onSurface = Moon,
    onSurfaceVariant = MoonMuted,
    outline = NightLine,
    error = DangerLight,        errorContainer = DangerDeep,
)

@Immutable
data class AutographExtraColors(
    val link: Color,
    val success: Color,
    val accent: Color,
    val halo: Color,
)

private val LightExtraColors = AutographExtraColors(
    link = IndigoDeep, success = Green, accent = Amber, halo = HaloLight,
)
private val DarkExtraColors = AutographExtraColors(
    link = IndigoSoft, success = GreenLight, accent = Amber, halo = HaloDark,
)

private val LocalExtraColors = staticCompositionLocalOf { LightExtraColors }

@Composable
fun AutographTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalExtraColors provides if (darkTheme) DarkExtraColors else LightExtraColors,
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = AutographTypography,
            content = content,
        )
    }
}

/** Read extra colors anywhere: AutographTheme.extraColors.link */
object AutographTheme {
    val extraColors: AutographExtraColors
        @Composable @ReadOnlyComposable
        get() = LocalExtraColors.current
}
