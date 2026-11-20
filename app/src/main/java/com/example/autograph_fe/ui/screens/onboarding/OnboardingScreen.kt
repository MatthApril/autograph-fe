package com.example.autograph_fe.ui.screens.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autograph_fe.R
import com.example.autograph_fe.ui.components.AutographButton
import com.example.autograph_fe.ui.components.PageIndicator
import com.example.autograph_fe.ui.theme.AutographTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    slides: List<OnboardingSlide>,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { slides.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == slides.lastIndex

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        // Top bar: logo mark left, Skip right
        Row(
            modifier = Modifier.fillMaxWidth().height(44.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_autograph_mark),
                contentDescription = stringResource(R.string.app_name),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(width = 28.dp, height = 16.dp),
            )
            Spacer(Modifier.weight(1f))
            TextButton(
                onClick = onFinish,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            ) {
                Text(stringResource(R.string.skip), style = MaterialTheme.typography.bodySmall)
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            OnboardingPage(slide = slides[page])
        }

        // Bottom row: dots left, button right
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PageIndicator(
                pageCount = slides.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(start = 8.dp),
            )
            Spacer(Modifier.weight(1f))
            AutographButton(
                text = stringResource(if (isLastPage) R.string.start_writing else R.string.onboarding_continue),
                trailingIcon = R.drawable.ic_arrow_forward,
                onClick = {
                    if (isLastPage) {
                        onFinish()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                },
            )
        }
    }
}

@Composable
fun OnboardingRoute(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = viewModel(factory = OnboardingViewModel.Factory),
) {
    OnboardingScreen(
        slides = onboardingSlides,
        onFinish = { viewModel.finishOnboarding(onSaved = onFinished) },
    )
}

@Composable
private fun OnboardingPage(slide: OnboardingSlide) {
    val iconTint = when (slide.tone) {
        SlideTone.Primary -> MaterialTheme.colorScheme.primary
        SlideTone.Accent -> AutographTheme.extraColors.accent
        SlideTone.Success -> AutographTheme.extraColors.success
    }

    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(12.dp))
        OnboardingStage(iconRes = slide.iconRes, iconTint = iconTint)

        // Text sits 32dp from the screen edge: the screen's 24dp plus 8dp here
        Column(Modifier.padding(horizontal = 8.dp)) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(slide.eyebrow).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(slide.title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(slide.body),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnboardingScreenPreview() {
    AutographTheme { OnboardingScreen(slides = onboardingSlides, onFinish = {}) }
}
