package com.app.smartcoffeemachine.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.smartcoffeemachine.common.ui.theme.PreviewAllVariants
import com.app.smartcoffeemachine.common.ui.theme.SmartCoffeeMachineTheme
import com.app.smartcoffeemachine.ui.viewmodel.MachineStatus

@Composable
fun StateDisplay(
    status: MachineStatus,
    modifier: Modifier = Modifier,
) {
    val color = when (status) {
        MachineStatus.IDLE -> SmartCoffeeMachineTheme.colors.outline

        MachineStatus.HEATING ->
            SmartCoffeeMachineTheme.colors.status.warning

        MachineStatus.READY ->
            SmartCoffeeMachineTheme.colors.status.success

        MachineStatus.BREWING ->
            SmartCoffeeMachineTheme.colors.content.primary

        MachineStatus.ERROR ->
            SmartCoffeeMachineTheme.colors.status.error
    }
    val animatedColor by animateColorAsState(
        animationSpec = tween(
            durationMillis = 1200
        ),
        targetValue = color,
        label = "stateColorAnimation"
    )
    Box(
        modifier = modifier
            .size(252.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        animatedColor.copy(alpha = 0.28f),
                        SmartCoffeeMachineTheme.colors.background
                    )
                )
            )
            .border(
                width = 1.dp,
                color = color,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(status.label),
            color = SmartCoffeeMachineTheme.colors.content.onSurface,
            style = SmartCoffeeMachineTheme.textStyle.displayMedium
        )
    }
}

@PreviewAllVariants
@Composable
private fun StateDisplayPreview() = SmartCoffeeMachineTheme {
    StateDisplay(
        status = MachineStatus.IDLE
    )
}