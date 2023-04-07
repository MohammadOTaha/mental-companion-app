package com.omar.mentalcompanion.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.omar.mentalcompanion.R

val InterTight = FontFamily(
    Font(R.font.intertight_regular, FontWeight.Normal),
    Font(R.font.intertight_medium, FontWeight.Medium),
    Font(R.font.intertight_semibold, FontWeight.SemiBold),
    Font(R.font.intertight_bold, FontWeight.Bold),
    Font(R.font.intertight_extrabold, FontWeight.ExtraBold),
    Font(R.font.intertight_black, FontWeight.Black)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = InterTight,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = InterTight,
        fontWeight = FontWeight.Black,
        fontSize = 40.sp,
        letterSpacing = 0.25.sp,
        lineHeight = 40.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterTight,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)