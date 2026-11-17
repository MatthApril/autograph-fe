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
    displaySmall = TextStyle(fontFamily = SourceSerif, fontWeight = FontWeight.Medium, fontSize = 31.sp, lineHeight = 36.sp),
    titleLarge   = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Bold, fontSize = 21.sp),
    bodyMedium   = TextStyle(fontFamily = Inter, fontSize = 14.sp, lineHeight = 21.sp),
    labelLarge   = TextStyle(fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
)