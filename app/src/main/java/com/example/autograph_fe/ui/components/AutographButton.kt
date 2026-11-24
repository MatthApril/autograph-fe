package com.example.autograph_fe.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

enum class ButtonVariant { Primary, Secondary, Text }

@Composable
fun AutographButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,                 // the caller decides width and position
    variant: ButtonVariant = ButtonVariant.Primary,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    @DrawableRes trailingIcon: Int? = null,
) {
    val sized = modifier.heightIn(min = 50.dp)
    val isClickable = enabled && !isLoading        // no taps while loading
    val label: @Composable () -> Unit = {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = LocalContentColor.current,     // same color the text would have
            )
        } else {
            Text(text, style = MaterialTheme.typography.labelLarge)
            if (trailingIcon != null) {
                Spacer(Modifier.width(8.dp))
                Icon(painterResource(trailingIcon), contentDescription = null, modifier = Modifier.size(20.dp))
            }
        }
    }

    when (variant) {
        ButtonVariant.Primary -> Button(
            // Stays enabled while loading so it keeps its color; taps are ignored instead
            onClick = { if (!isLoading) onClick() },
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                // The mockup's disabled look: the normal indigo, faded, not Material's gray
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = sized.dropShadow(
                shape = RoundedCornerShape(50),
                shadow = Shadow(
                    radius = 22.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = if (enabled) 0.22f else 0f),
                    offset = DpOffset(0.dp, 8.dp),
                ),
            ),
        ) { label() }

        ButtonVariant.Secondary -> OutlinedButton(
            onClick = onClick, modifier = sized, enabled = isClickable,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) { label() }

        ButtonVariant.Text -> TextButton(
            onClick = onClick, modifier = sized, enabled = isClickable,
        ) { label() }
    }
}
