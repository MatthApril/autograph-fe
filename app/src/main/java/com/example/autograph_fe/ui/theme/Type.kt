package com.example.autograph_fe.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.autograph_fe.R

val SourceSerif = FontFamily(Font(R.font.source_serif_4_36pt_medium, FontWeight.Medium))
val Inter = FontFamily(
    Font(R.font.inter_18pt_regular, FontWeight.Normal),
    Font(R.font.inter_18pt_semi_bold, FontWeight.SemiBold),
    Font(R.font.inter_18pt_bold, FontWeight.Bold),
)

val AutographTypography = Typography(
    // Serif titles
    displaySmall   = TextStyle(fontFamily = SourceSerif, fontWeight = FontWeight.Medium, fontSize = 31.sp, lineHeight = 36.sp),   // Welcome
    headlineLarge  = TextStyle(fontFamily = SourceSerif, fontWeight = FontWeight.Medium, fontSize = 30.sp, lineHeight = 31.5.sp, letterSpacing = (-0.6).sp), // Onboarding
    headlineMedium = TextStyle(fontFamily = SourceSerif, fontWeight = FontWeight.Medium, fontSize = 27.sp, lineHeight = 32.sp),   // Login, Register
    // Inter
    titleLarge  = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Bold, fontSize = 21.sp),                          // Logo wordmark
    titleSmall  = TextStyle(fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 21.sp),  // Top bar title
    bodyMedium  = TextStyle(fontFamily = Inter, fontSize = 14.sp, lineHeight = 21.sp),                                    // Subtitles, typed text
    bodySmall   = TextStyle(fontFamily = Inter, fontSize = 13.sp, lineHeight = 19.5.sp),                                  // Onboarding body, Skip
    labelLarge  = TextStyle(fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),                      // Main buttons
    labelMedium = TextStyle(fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 18.sp),  // Field labels, links
    labelSmall  = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.8.sp),  // Eyebrows: CAPTURE
)