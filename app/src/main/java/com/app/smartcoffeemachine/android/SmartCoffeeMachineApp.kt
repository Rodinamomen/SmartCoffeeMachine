package com.app.smartcoffeemachine.android

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.smartcoffeemachine.ui.navigation.SetupNavGraph
import com.app.smartcoffeemachine.common.ui.theme.SmartCoffeeMachineTheme

@Composable
fun BaseApp(navController: NavHostController = rememberNavController()) {
    SmartCoffeeMachineTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SetupNavGraph(navController = navController)
        }
    }
}