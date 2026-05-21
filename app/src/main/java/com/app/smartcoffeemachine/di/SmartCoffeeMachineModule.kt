package com.app.smartcoffeemachine.di

import com.app.smartcoffeemachine.common.data.repo.local.database.AppDatabase
import com.app.smartcoffeemachine.data.repo.remote.SmartCoffeeMachineRemoteDataSource
import com.app.smartcoffeemachine.data.repo.SmartCoffeeMachineRepository
import com.app.smartcoffeemachine.data.repo.local.SmartCoffeeMachineLocalDataSource
import com.app.smartcoffeemachine.domain.repo.local.ISmartCoffeeMachineLocalDataSource
import com.app.smartcoffeemachine.domain.repo.remote.ISmartCoffeeMachineRemoteDataSource
import com.app.smartcoffeemachine.domain.repo.ISmartCoffeeMachineRepository
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager
import com.app.smartcoffeemachine.domain.statemachine.state.BrewingState
import com.app.smartcoffeemachine.domain.statemachine.state.ErrorState
import com.app.smartcoffeemachine.domain.statemachine.state.HeatingState
import com.app.smartcoffeemachine.domain.statemachine.state.IdealState
import com.app.smartcoffeemachine.domain.statemachine.state.ReadyState
import com.app.smartcoffeemachine.domain.usecase.AutomaticErrorUseCase
import com.app.smartcoffeemachine.domain.usecase.BrewingUseCase
import com.app.smartcoffeemachine.domain.usecase.PowerOnUseCase
import com.app.smartcoffeemachine.domain.usecase.SaveBrewUseCase
import com.app.smartcoffeemachine.domain.usecase.LogTransactionUseCase
import com.app.smartcoffeemachine.ui.viewmodel.SmartCoffeeMachineViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val smartCoffeeMachineModule = module {
    singleOf(::SmartCoffeeMachineRemoteDataSource) bind ISmartCoffeeMachineRemoteDataSource::class
    singleOf(::SmartCoffeeMachineLocalDataSource) bind ISmartCoffeeMachineLocalDataSource::class
    singleOf(::SmartCoffeeMachineRepository) bind ISmartCoffeeMachineRepository::class
    single { get<AppDatabase>().BrewEntityDao() }
    factoryOf(::IdealState)
    factoryOf(::HeatingState)
    factoryOf(::ReadyState)
    factoryOf(::BrewingState)
    factoryOf(::ErrorState)
    singleOf(::StateMachineManager)
    singleOf(::LogTransactionUseCase)
    factoryOf(::PowerOnUseCase)
    factoryOf(::BrewingUseCase)
    factoryOf(::AutomaticErrorUseCase)
    singleOf(::BrewingUseCase)
    factoryOf(::SaveBrewUseCase)
    viewModelOf(::SmartCoffeeMachineViewModel)
}