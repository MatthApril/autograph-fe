package com.example.autograph_fe.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class ButtonVariant { Primary, Secondary, Text }

@Composable
fun AutographButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,                 // the caller decides width and position
    variant: ButtonVariant = ButtonVariant.Primary,
    enabled: Boolean = true,
) {
    val sized = modifier.heightIn(min = 50.dp)
    val label: @Composable () -> Unit = {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }

    when (variant) {
        ButtonVariant.Primary -> Button(
            onClick = onClick, modifier = sized, enabled = enabled,
        ) { label() }

        ButtonVariant.Secondary -> OutlinedButton(
            onClick = onClick, modifier = sized, enabled = enabled,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) { label() }

        ButtonVariant.Text -> TextButton(
            onClick = onClick, modifier = sized, enabled = enabled,
        ) { label() }
    }
}