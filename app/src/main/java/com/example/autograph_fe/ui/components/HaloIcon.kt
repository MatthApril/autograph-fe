package com.example.autograph_fe.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.autograph_fe.ui.theme.AutographTheme

/** The slightly uneven "pebble" shape of illustration blobs. */
val PebbleShape = RoundedCornerShape(
    topStartPercent = 44, topEndPercent = 56, bottomEndPercent = 48, bottomStartPercent = 52,
)

/** An icon in a white pebble inside a pale halo: the envelope on Verify, the check on Verified. */
@Composable
fun HaloIcon(
    @DrawableRes iconRes: Int,
    iconTint: Color,
    modifier: Modifier = Modifier,
    haloSize: Dp = 120.dp,
    iconSize: Dp = 42.dp,
) {
    val colors = MaterialTheme.colorScheme
    val isLight = colors.background.luminance() > 0.5f

    Box(
        modifier = modifier
            .size(haloSize)
            .background(AutographTheme.extraColors.halo.copy(alpha = 0.72f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                // The pebble is 70% of the halo wide and a little flatter, as in the mockup
                .size(width = haloSize * 0.7f, height = haloSize * 0.64f)
                .then(
                    if (isLight) {
                        Modifier.dropShadow(
                            shape = PebbleShape,
                            shadow = Shadow(
                                radius = 40.dp,
                                color = Color(0xFF2E2A26).copy(alpha = 0.08f),
                                offset = DpOffset(0.dp, 12.dp),
                            ),
                        )
                    } else Modifier
                )
                .background(colors.surface, PebbleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}
