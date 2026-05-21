package com.app.smartcoffeemachine.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.smartcoffeemachine.R
import com.app.smartcoffeemachine.common.ui.theme.SmartCoffeeMachineTheme

@Composable
fun MachineError(
    errorTitle : String,
    errorCode: String ,
    onResetClicked : () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SmartCoffeeMachineTheme.colors.status.error.copy(0.6f),
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = SmartCoffeeMachineTheme.colors.status.error,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MachineErrorIcon()
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = errorTitle,
                    color = SmartCoffeeMachineTheme.colors.content.onSurface,
                    style = SmartCoffeeMachineTheme.textStyle.titleMedium
                )
                Text(
                    text = errorCode,
                    color = SmartCoffeeMachineTheme.colors.status.error,
                    style = SmartCoffeeMachineTheme.textStyle.bodySmall
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.error_message).plus(" "),
                color = SmartCoffeeMachineTheme.colors.content.onSurfaceVar,
                style = SmartCoffeeMachineTheme.textStyle.titleMedium
            )
            Text(
                modifier = Modifier.clickable{
                    onResetClicked()
                },
                text = stringResource(R.string.reset),
                color = SmartCoffeeMachineTheme.colors.status.error,
                style = SmartCoffeeMachineTheme.textStyle.labelMedium,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
private fun MachineErrorIcon() {
    Box(
        modifier = Modifier
            .size(34.dp)
            .background(
                color = SmartCoffeeMachineTheme.colors.status.error.copy(0.16f),
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = SmartCoffeeMachineTheme.colors.status.error,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.height(24.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_error),
            contentDescription = null,
            tint = SmartCoffeeMachineTheme.colors.status.error
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MachineErrorPreview() = SmartCoffeeMachineTheme {
    MachineError(
        errorTitle = "Network timeout",
        errorCode = "NET-408",
        onResetClicked = {}
    )
}