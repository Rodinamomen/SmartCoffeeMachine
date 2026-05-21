package com.app.smartcoffeemachine.common.di

import com.app.smartcoffeemachine.di.smartCoffeeMachineModule
import org.koin.dsl.module

val featuresModule = module {
    includes(smartCoffeeMachineModule)
}