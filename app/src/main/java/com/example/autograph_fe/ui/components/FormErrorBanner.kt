package com.example.autograph_fe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormErrorBanner(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onErrorContainer,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
    )
}
