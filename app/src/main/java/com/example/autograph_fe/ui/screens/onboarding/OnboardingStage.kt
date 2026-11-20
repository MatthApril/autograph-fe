package com.example.autograph_fe.ui.screens.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.autograph_fe.ui.theme.AutographTheme

private val BlobShape = RoundedCornerShape(
    topStartPercent = 44, topEndPercent = 56, bottomEndPercent = 53, bottomStartPercent = 47,
)

@Composable
fun OnboardingStage(
    @DrawableRes iconRes: Int,
    iconTint: Color,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val isLight = colors.background.luminance() > 0.5f   // the blob only casts a shadow in light mode

    Box(
        modifier = modifier.fillMaxWidth().height(330.dp),
        contentAlignment = Alignment.Center,               // every layer is centered on the same point
    ) {
        Box(Modifier.size(280.dp).background(AutographTheme.extraColors.halo, CircleShape))

        Orbit(width = 206.dp, height = 110.dp, rotation = 28f)
        Orbit(width = 255.dp, height = 158.dp, rotation = -32f)

        Box(
            modifier = Modifier
                .size(140.dp)
                .then(
                    if (isLight) {
                        Modifier.dropShadow(
                            shape = BlobShape,
                            shadow = Shadow(
                                radius = 40.dp,
                                color = Color(0xFF2E2A26).copy(alpha = 0.08f),
                                offset = DpOffset(0.dp, 12.dp),
                            ),
                        )
                    } else Modifier
                )
                .background(colors.surface, BlobShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(42.dp),
            )
        }
    }
}

@Composable
private fun Orbit(width: Dp, height: Dp, rotation: Float) {
    val lineColor = MaterialTheme.colorScheme.outline
    Box(
        modifier = Modifier
            .size(width, height)
            .rotate(rotation)
            .drawBehind {
                drawOval(color = lineColor, style = Stroke(width = 1.dp.toPx()))
            },
    ) {
        // The dot rotates with its ring because it's inside the rotated Box
        Box(
            Modifier
                .offset(x = 42.dp, y = 8.dp)
                .size(12.dp)
                .background(AutographTheme.extraColors.accent, CircleShape)
        )
    }
}
