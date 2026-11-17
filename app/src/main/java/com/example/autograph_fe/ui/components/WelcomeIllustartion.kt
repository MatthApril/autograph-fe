package com.example.autograph_fe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.autograph_fe.ui.theme.Amber
import com.example.autograph_fe.ui.theme.softShadow

@Composable
fun WelcomeIllustration(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val cardShape = RoundedCornerShape(20.dp)
    val cardSize = Modifier.size(width = 140.dp, height = 165.dp)

    Box(modifier.size(220.dp), contentAlignment = Alignment.Center) {

        // Back card: tilted right, tinted with the primary color
        Box(
            cardSize
                .offset(x = 22.dp, y = 6.dp)
                .rotate(6f)
                .softShadow(cardShape)
                .background(colors.surface, cardShape)
                .background(colors.primary.copy(alpha = 0.12f), cardShape)
                .border(1.dp, colors.outline, cardShape)
        )

        // Front card: tilted left, with text lines and a circle
        Column(
            cardSize
                .offset(x = (-8).dp)
                .rotate(-5f)
                .softShadow(cardShape)
                .background(colors.surface, cardShape)
                .border(1.dp, colors.outline, cardShape)
                .padding(20.dp)
        ) {
            listOf(1f, 0.75f, 0.9f).forEach { widthFraction ->
                Box(
                    Modifier
                        .fillMaxWidth(widthFraction)
                        .height(6.dp)
                        .background(colors.outline, CircleShape)
                )
                Spacer(Modifier.height(12.dp))
            }
            Spacer(Modifier.weight(1f))
            Box(
                Modifier
                    .align(Alignment.End)
                    .size(36.dp)
                    .border(3.dp, Amber, CircleShape)
            )
        }
    }
}