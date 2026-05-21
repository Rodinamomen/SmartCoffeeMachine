package com.app.smartcoffeemachine.ui.view

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.smartcoffeemachine.R
import com.app.smartcoffeemachine.common.ui.theme.SmartCoffeeMachineTheme
import com.app.smartcoffeemachine.ui.viewmodel.SmartCoffeeMachineViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SmartCoffeeMachine(
    viewModel: SmartCoffeeMachineViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SmartCoffeeMachineContent(state = state, action = viewModel::onActionTrigger)
}

@Composable
fun SmartCoffeeMachineContent(
    state: SmartCoffeeMachineContract.SmartCoffeeMachineState,
    action: (SmartCoffeeMachineContract.SmartCoffeeMachineAction) -> Unit,
) {
    if(state.errorCause.isNotEmpty()){
        Text(
            text = state.errorCause,
            color = SmartCoffeeMachineTheme.colors.content.onSurface,
            style = SmartCoffeeMachineTheme.textStyle.displayMedium
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = SmartCoffeeMachineTheme.colors.background)
            .padding(horizontal = 16.dp)
            .padding(top= 75.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StateDisplay(status = state.status)
        AnimatedVisibility(
            state.progress!= 0
        ) {
            LinearProgressIndicator(
                progress = { state.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50)),
                color = SmartCoffeeMachineTheme.colors.status.warning,
                trackColor = SmartCoffeeMachineTheme.colors.outline
            )
        }
       StatusButton(
            onClick = { action(SmartCoffeeMachineContract.SmartCoffeeMachineAction.PowerOn) },
            enabled = state.isPowerOnEnabled,
            color = SmartCoffeeMachineTheme.colors.content.onSurface,
            contentColor = SmartCoffeeMachineTheme.colors.surfaceContainer,
            label = stringResource(R.string.power_on)
        )
        AnimatedVisibility(
            state.isStartBrewEnabled
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BrewTypeCard(
                    modifier = Modifier.padding(end = 16.dp),
                    isSelected = true,
                    icon = R.drawable.ic_espresso,
                    text = stringResource(R.string.espresso),
                )
                BrewTypeCard(
                    isSelected = false,
                    icon = R.drawable.ic_latte,
                    text = stringResource(R.string.latte),
                )
        }
        }
        StatusButton(
            onClick = { action(SmartCoffeeMachineContract.SmartCoffeeMachineAction.StartBrew)},
            enabled = state.isStartBrewEnabled,
            color = SmartCoffeeMachineTheme.colors.content.onSurface,
            contentColor = SmartCoffeeMachineTheme.colors.surfaceContainer,
            label = stringResource(R.string.start_brew)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            StatusButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    action(SmartCoffeeMachineContract.SmartCoffeeMachineAction.CancelBrew)
                },
                enabled = state.isCancelBrewEnabled,
                color = SmartCoffeeMachineTheme.colors.status.error,
                contentColor = SmartCoffeeMachineTheme.colors.content.onSurface,
                label = stringResource(R.string.cancel)
            )

            StatusButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    action(SmartCoffeeMachineContract.SmartCoffeeMachineAction.AutomaticError)
                },
                enabled = state.isCancelBrewEnabled,
                color = SmartCoffeeMachineTheme.colors.status.error,
                contentColor = SmartCoffeeMachineTheme.colors.content.onSurface,
                label = stringResource(R.string.automatic_error)
            )
        }
        StatusButton(
            onClick = {  action(SmartCoffeeMachineContract.SmartCoffeeMachineAction.Reset) },
            enabled = state.isResetEnabled,
            color = SmartCoffeeMachineTheme.colors.content.onSurface,
            contentColor = SmartCoffeeMachineTheme.colors.surfaceContainer,
            label = stringResource(R.string.reset)
        )
    }
}

@Composable
private fun StateDisplay(
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

@Composable
private fun StatusButton(
    onClick: () -> Unit,
    enabled: Boolean,
    contentColor: Color,
    color: Color,
    label: String,
    modifier: Modifier = Modifier
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
private fun BrewTypeCard(
    isSelected: Boolean,
    @DrawableRes icon: Int,
    text: String,
    modifier: Modifier = Modifier,
) {

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            SmartCoffeeMachineTheme.colors.content.onSurface
        } else {
            SmartCoffeeMachineTheme.colors.outline
        },
        animationSpec = tween(500),
        label = "borderColorAnimation"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            SmartCoffeeMachineTheme.colors.outline
        } else {
            SmartCoffeeMachineTheme.colors.background
        },
        animationSpec = tween(500),
        label = "backgroundColorAnimation"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) {
            SmartCoffeeMachineTheme.colors.content.onSurface
        } else {
            SmartCoffeeMachineTheme.colors.outline
        },
        animationSpec = tween(500),
        label = "contentColorAnimation"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .size(width = 90.dp, height = 86.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = text,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )

        Text(
            modifier= Modifier.padding(top= 6.dp),
            text = text,
            color = contentColor,
            style = SmartCoffeeMachineTheme.textStyle.titleMedium
        )
    }
}

@Composable
@Preview
fun SmartCoffeeMachineContentPreview() = SmartCoffeeMachineTheme {
    SmartCoffeeMachineContent(
        state = SmartCoffeeMachineContract.SmartCoffeeMachineState(),
        action = {}
    )
}