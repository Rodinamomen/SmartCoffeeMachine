package com.app.smartcoffeemachine.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.app.smartcoffeemachine.common.ui.theme.color.SmartCoffeeColors
import com.app.smartcoffeemachine.common.ui.theme.color.darkScheme
import com.app.smartcoffeemachine.common.ui.theme.color.lightScheme
import com.app.smartcoffeemachine.common.ui.theme.color.localColors
import com.app.smartcoffeemachine.common.ui.theme.font.SmartCoffeeTypography
import com.app.smartcoffeemachine.common.ui.theme.font.localTextStyle
import com.app.smartcoffeemachine.common.ui.theme.font.smartCoffeeTypography

@Composable
fun SmartCoffeeMachineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme
    val textStyle = smartCoffeeTypography()
    CompositionLocalProvider(
        localColors provides colorScheme,
        localTextStyle provides textStyle,
    ) {
        content()
    }
}

object SmartCoffeeMachineTheme {
    val colors: SmartCoffeeColors
        @Composable get() = localColors.current
    val textStyle: SmartCoffeeTypography
        @Composable get() = localTextStyle.current
}