package com.app.smartcoffeemachine.common.ui.theme.font

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class SmartCoffeeTypography(
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
)

@Composable
fun smartCoffeeTypography(): SmartCoffeeTypography {
    val fontFamily = SmartCoffeeFontFamily()

    fun style(size: TextUnit, weight: FontWeight) = TextStyle(
        fontFamily = fontFamily,
        fontSize = size,
        fontWeight = weight
    )

    return SmartCoffeeTypography(
        displayLarge = style(32.sp, FontWeight.Bold),
        displayMedium = style(24.sp, FontWeight.Bold),
        headlineLarge = style(20.sp, FontWeight.Bold),
        headlineMedium = style(18.sp, FontWeight.SemiBold),
        titleLarge = style(16.sp, FontWeight.Bold),
        titleMedium = style(14.sp, FontWeight.Medium),
        bodyLarge = style(16.sp, FontWeight.Normal),
        bodyMedium = style(14.sp, FontWeight.Normal),
        bodySmall = style(12.sp, FontWeight.Normal),
        labelMedium = style(12.sp, FontWeight.Medium),
        labelSmall = style(10.sp, FontWeight.Medium),
    )
}

val localTextStyle = staticCompositionLocalOf<SmartCoffeeTypography> { error("Cannot provide text style") }