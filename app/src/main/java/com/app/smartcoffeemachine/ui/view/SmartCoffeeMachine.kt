package com.app.smartcoffeemachine.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.smartcoffeemachine.R
import com.app.smartcoffeemachine.common.ui.theme.PreviewAllVariants
import com.app.smartcoffeemachine.common.ui.theme.SmartCoffeeMachineTheme
import com.app.smartcoffeemachine.domain.model.BrewType
import com.app.smartcoffeemachine.ui.components.BrewTypeCard
import com.app.smartcoffeemachine.ui.components.MachineError
import com.app.smartcoffeemachine.ui.components.StateDisplay
import com.app.smartcoffeemachine.ui.components.StatusButton
import com.app.smartcoffeemachine.ui.viewmodel.SmartCoffeeMachineContract
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = SmartCoffeeMachineTheme.colors.background)
            .padding(horizontal = 16.dp)
            .padding(top = 75.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StateDisplay(status = state.status)
        AnimatedVisibility(
            state.progress != 0
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
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(state.brewTypes) { brew ->
                    BrewTypeCard(
                        isSelected = brew.isSelected,
                        icon = when (brew.brewType) {
                            BrewType.ESPRESSO -> R.drawable.ic_espresso
                            BrewType.LATTE -> R.drawable.ic_latte
                        },
                        text = brew.brewType.name,
                        onClick = {
                            action(
                                SmartCoffeeMachineContract
                                    .SmartCoffeeMachineAction
                                    .SelectBrewType(brew.brewType)
                            )
                        }
                    )
                }
            }
        }
        StatusButton(
            onClick = { action(SmartCoffeeMachineContract.SmartCoffeeMachineAction.StartBrew) },
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
        AnimatedVisibility(
            visible = state.isErrorVisible
        ) {
            MachineError(
                errorTitle = state.errorUiState.errorTitle,
                errorCode = state.errorUiState.errorMessage,
                onResetClicked = { action(SmartCoffeeMachineContract.SmartCoffeeMachineAction.Reset) },
            )
        }
    }
}

@Composable
@PreviewAllVariants
private fun SmartCoffeeMachineContentPreview() = SmartCoffeeMachineTheme {
    SmartCoffeeMachineContent(
        state = SmartCoffeeMachineContract.SmartCoffeeMachineState(),
        action = {}
    )
}