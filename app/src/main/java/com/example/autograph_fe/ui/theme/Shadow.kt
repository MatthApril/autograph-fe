package com.example.autograph_fe.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

fun Modifier.softShadow(shape: Shape) = this.dropShadow(
    shape = shape,
    shadow = Shadow(
        radius = 24.dp,
        color = Color.Black.copy(alpha = 0.06f),
        offset = DpOffset(0.dp, 10.dp),
    ),
)