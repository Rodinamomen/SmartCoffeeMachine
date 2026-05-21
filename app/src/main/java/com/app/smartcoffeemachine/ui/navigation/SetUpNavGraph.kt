package com.app.smartcoffeemachine.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.smartcoffeemachine.ui.view.SmartCoffeeMachine

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.SmartCoffeeMachine) {
        composable<Screen.SmartCoffeeMachine> {
            SmartCoffeeMachine()
        }
    }
}