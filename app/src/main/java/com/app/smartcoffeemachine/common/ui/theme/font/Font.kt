package com.app.smartcoffeemachine.common.ui.theme.font

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.app.smartcoffeemachine.R

@Composable
fun SmartCoffeeFontFamily(): FontFamily {
    return FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold),
        Font(R.font.roboto_semibold, FontWeight.Medium),
        Font(R.font.roboto_extrabold, FontWeight.Normal),
        Font(R.font.roboto_medium, FontWeight.Bold),
        Font(R.font.roboto_regular, FontWeight.Medium),
        Font(R.font.roboto_extralight, FontWeight.Normal),
        Font(R.font.roboto_light, FontWeight.Normal),
    )
}