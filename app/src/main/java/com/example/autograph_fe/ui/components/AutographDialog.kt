package com.example.autograph_fe.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.example.autograph_fe.R
import com.example.autograph_fe.ui.theme.AutographTheme

/**
 * The app's confirmation dialog: optional icon, serif title, short message, a primary button
 * and an optional text link. Reuse it for every confirmation, like "Delete note?".
 */
@Composable
fun AutographDialog(
    title: String,
    message: String,
    primaryText: String,
    onPrimaryClick: () -> Unit,
    onDismissRequest: () -> Unit,
    @DrawableRes iconRes: Int? = null,
    secondaryText: String? = null,
    onSecondaryClick: () -> Unit = onDismissRequest,
) {
    Dialog(
        onDismissRequest = onDismissRequest,                              // back button or tap outside
        properties = DialogProperties(usePlatformDefaultWidth = false),   // we set our own width
    ) {
        // Android dims the screen 60% behind a dialog; the mockup uses a lighter 36%
        val window = (LocalView.current.parent as? DialogWindowProvider)?.window
        SideEffect { window?.setDimAmount(0.36f) }

        val colors = MaterialTheme.colorScheme
        val shape = RoundedCornerShape(18.dp)

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .dropShadow(
                    shape = shape,
                    shadow = Shadow(radius = 55.dp, color = Color.Black.copy(alpha = 0.22f), offset = DpOffset(0.dp, 20.dp)),
                )
                .background(colors.surface, shape)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (iconRes != null) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(AutographTheme.extraColors.halo, RoundedCornerShape(15.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(painterResource(iconRes), contentDescription = null, tint = colors.primary, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.height(14.dp))
            }
            Text(title, style = MaterialTheme.typography.titleMedium, color = colors.onSurface, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(message, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant, textAlign = TextAlign.Center)
            Spacer(Modifier.height(20.dp))
            AutographButton(text = primaryText, onClick = onPrimaryClick, modifier = Modifier.widthIn(min = 160.dp))
            if (secondaryText != null) {
                Spacer(Modifier.height(4.dp))
                TextLink(text = secondaryText, onClick = onSecondaryClick)
            }
        }
    }
}

@Preview
@Composable
private fun AutographDialogPreview() = AutographTheme {
    AutographDialog(
        title = "Leave verification?",
        message = "You'll need to sign up again if the code expires.",
        primaryText = "Stay",
        onPrimaryClick = {},
        onDismissRequest = {},
        iconRes = R.drawable.ic_lock,
        secondaryText = "Leave",
    )
}
