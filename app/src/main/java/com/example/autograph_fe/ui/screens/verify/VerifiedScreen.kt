package com.example.autograph_fe.ui.screens.verify

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autograph_fe.R
import com.example.autograph_fe.ui.components.AutographButton
import com.example.autograph_fe.ui.components.HaloIcon
import com.example.autograph_fe.ui.components.ThemeToggleButton
import com.example.autograph_fe.ui.theme.AutographTheme

@Composable
fun VerifiedRoute(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onContinue: () -> Unit,
    viewModel: VerifiedViewModel = viewModel(factory = VerifiedViewModel.Factory),
) {
    // There's nothing to go back to (the sign-up screens are cleared), so back means "continue"
    BackHandler(onBack = onContinue)

    VerifiedScreen(
        firstName = viewModel.firstName,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        onContinue = onContinue,
    )
}

@Composable
fun VerifiedScreen(
    firstName: String,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onContinue: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    // A gentle pop-in when the screen appears
    val scale = remember { Animatable(0.85f) }
    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // No back arrow here; only the theme toggle, top right
        Box(Modifier.fillMaxWidth().height(44.dp)) {
            ThemeToggleButton(
                isDark = isDarkTheme,
                onToggle = onToggleTheme,
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }

        Spacer(Modifier.weight(1f))
        HaloIcon(
            iconRes = R.drawable.ic_check,
            iconTint = AutographTheme.extraColors.success,
            haloSize = 154.dp,
            modifier = Modifier.graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        )
        Spacer(Modifier.height(30.dp))
        Text(
            text = stringResource(R.string.verified_eyebrow).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = colors.primary,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = if (firstName.isBlank()) stringResource(R.string.verified_title_no_name)
            else stringResource(R.string.verified_title, firstName),
            style = MaterialTheme.typography.headlineMedium,
            color = colors.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.verified_body),
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 300.dp),
        )
        Spacer(Modifier.weight(2.5f))

        AutographButton(
            text = stringResource(R.string.onboarding_continue),
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = R.drawable.ic_arrow_forward,
        )
    }
}

@Preview(name = "Light", showBackground = true, heightDp = 844)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, heightDp = 844)
@Composable
private fun VerifiedScreenPreview() = AutographTheme {
    VerifiedScreen(firstName = "Maya", isDarkTheme = isSystemInDarkTheme(), onToggleTheme = {}, onContinue = {})
}
