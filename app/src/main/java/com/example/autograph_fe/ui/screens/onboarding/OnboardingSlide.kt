package com.example.autograph_fe.ui.screens.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.autograph_fe.R

/** Which theme color the slide's icon uses. */
enum class SlideTone { Primary, Accent, Success }

data class OnboardingSlide(
    @DrawableRes val iconRes: Int,
    val tone: SlideTone,
    @StringRes val eyebrow: Int,
    @StringRes val title: Int,
    @StringRes val body: Int,
)

val onboardingSlides = listOf(
    OnboardingSlide(
        R.drawable.ic_description, SlideTone.Primary,
        R.string.onboarding_1_eyebrow, R.string.onboarding_1_title, R.string.onboarding_1_body,
    ),
    OnboardingSlide(
        R.drawable.ic_edit, SlideTone.Accent,
        R.string.onboarding_2_eyebrow, R.string.onboarding_2_title, R.string.onboarding_2_body,
    ),
    OnboardingSlide(
        R.drawable.ic_drag_indicator, SlideTone.Success,
        R.string.onboarding_3_eyebrow, R.string.onboarding_3_title, R.string.onboarding_3_body,
    ),
)
