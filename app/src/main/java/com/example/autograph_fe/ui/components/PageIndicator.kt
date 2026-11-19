package com.example.autograph_fe.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autograph_fe.R
import com.example.autograph_fe.ui.theme.AutographTheme
import androidx.compose.runtime.getValue

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(R.string.page_indicator, currentPage + 1, pageCount)

    Row(
        modifier = modifier.semantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            val width by animateDpAsState(
                targetValue = if (isSelected) 20.dp else 6.dp,
                label = "dotWidth",
            )
            Box(
                Modifier
                    .size(width = width, height = 6.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PageIndicatorPreview() {
    AutographTheme { PageIndicator(pageCount = 3, currentPage = 1) }
}