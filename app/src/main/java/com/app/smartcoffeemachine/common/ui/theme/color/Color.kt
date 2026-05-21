package com.app.smartcoffeemachine.common.ui.theme.color

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class Status(
    val success: Color,
    val warning: Color,
    val error: Color,
    val disabled: Color,
)
data class Content(
    val onSurface: Color,
    val primary: Color,
    val onSurfaceVar: Color,
    val tertiaryText: Color,
)

data class SmartCoffeeColors(
    val background: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val outline: Color,
    val content: Content,
    val status : Status

)

val lightStatus = Status(
    success = Color(0xFF7ED957),
    warning = Color(0xFFFFB547),
    error = Color(0xFFFF5A5A),
    disabled = Color(0x40F5E6CA)
)

val lightContent = Content(
    onSurface = Color(0xFFF5E6CA),
    primary =Color(0xFF6F4E37),
    onSurfaceVar = Color(0x9EF5E6CA),
    tertiaryText =  Color(0xD4F5E6CA),
)
val lightScheme = SmartCoffeeColors(
    background = Color(0xFF0E0B09),
    surfaceContainer = Color(0xFF1A1411),
    surfaceContainerHigh = Color(0xFF221915),
    outline = Color(0x14F5E6CA),
    content = lightContent,
    status = lightStatus
)
val darkStatus = Status(
    success = Color(0xFF7ED957),
    warning = Color(0xFFFFB547),
    error = Color(0xFFFF5A5A),
    disabled = Color(0x40F5E6CA)
)
val darkContent = Content(
    onSurface = Color(0xFFF5E6CA),
    primary =Color(0xFF6F4E37),
    onSurfaceVar = Color(0x9EF5E6CA),
    tertiaryText =  Color(0xD4F5E6CA),
)
val darkScheme = SmartCoffeeColors(
    background = Color(0xFF0E0B09),
    surfaceContainer = Color(0xFF1A1411),
    surfaceContainerHigh = Color(0xFF221915),
    outline = Color(0x14F5E6CA),
    content = darkContent,
    status = darkStatus
)

val localColors = staticCompositionLocalOf<SmartCoffeeColors> { error("Cannot provide colors") }