package com.example.autograph_fe.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autograph_fe.R
import com.example.autograph_fe.ui.theme.AutographTheme

@Composable
fun AutographLogo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_autograph_mark),
            contentDescription = null,   // the word next to it already says "Autograph"
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(width = 34.dp, height = 20.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, backgroundColor = 0xFF121316, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AutographLogoPreview() {
    AutographTheme {
        AutographLogo()
    }
}