package com.app.smartcoffeemachine.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.app.smartcoffeemachine.common.ui.theme.PreviewAllVariants
import com.app.smartcoffeemachine.common.ui.theme.SmartCoffeeMachineTheme

@Composable
fun StatusButton(
    label: String,
    enabled: Boolean,
    contentColor: Color,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = contentColor,
        ),
        onClick = {
            onClick()
        },
        enabled = enabled

    ) {
        Text(
            text = label,
            style = SmartCoffeeMachineTheme.textStyle.titleLarge
        )
    }
}
@Composable
@PreviewAllVariants
private fun StatusButtonPreview() = SmartCoffeeMachineTheme{
    StatusButton(
        label = "Power On",
        enabled = true,
        contentColor = SmartCoffeeMachineTheme.colors.content.onSurface,
        color = SmartCoffeeMachineTheme.colors.surfaceContainer,
        onClick = {}
    )
}