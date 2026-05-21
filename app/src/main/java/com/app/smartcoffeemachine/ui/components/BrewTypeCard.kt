package com.app.smartcoffeemachine.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.smartcoffeemachine.R
import com.app.smartcoffeemachine.common.ui.theme.PreviewAllVariants
import com.app.smartcoffeemachine.common.ui.theme.SmartCoffeeMachineTheme

@Composable
fun BrewTypeCard(
    isSelected: Boolean,
    @DrawableRes icon: Int,
    text: String,
    onClick: () -> Unit,
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
            .clickable { onClick() }
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
            modifier = Modifier.padding(top = 6.dp),
            text = text,
            color = contentColor,
            style = SmartCoffeeMachineTheme.textStyle.titleMedium
        )
    }
}

@Composable
@PreviewAllVariants
private fun BrewTypeCardPreview() = SmartCoffeeMachineTheme {
    BrewTypeCard(
        isSelected = true,
        icon = R.drawable.ic_latte,
        text = "latte",
        onClick = {},
    )
}