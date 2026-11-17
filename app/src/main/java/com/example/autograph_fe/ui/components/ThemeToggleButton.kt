package com.example.autograph_fe.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.autograph_fe.R
import com.example.autograph_fe.ui.theme.AutographTheme

@Composable
fun ThemeToggleButton(
    isDark: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Show the mode you'll switch TO: a moon in light mode, a sun in dark mode.
    val iconRes = if (isDark) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
    val description = stringResource(
        if (isDark) R.string.switch_to_light_mode else R.string.switch_to_dark_mode
    )

    IconButton(onClick = onToggle, modifier = modifier) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = description,   // read aloud by screen readers
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, backgroundColor = 0xFF121316, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ThemeToggleButtonPreview() {
    AutographTheme {
        ThemeToggleButton(isDark = isSystemInDarkTheme(), onToggle = {})
    }
}