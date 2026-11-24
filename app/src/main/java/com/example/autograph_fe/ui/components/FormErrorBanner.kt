package com.example.autograph_fe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Red banner for network or server errors. Matches the mockup: small text, thin red border. */
@Composable
fun FormErrorBanner(message: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(10.dp)
    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
        color = MaterialTheme.colorScheme.error,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer, shape)
            .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f), shape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
    )
}
