package com.app.smartcoffeemachine.domain.usecase

import com.app.smartcoffeemachine.domain.model.Brew
import com.app.smartcoffeemachine.domain.repo.ISmartCoffeeMachineRepository

class SaveBrewUseCase(private val repository: ISmartCoffeeMachineRepository) {
    suspend operator fun invoke(domain: Brew) {
        repository.saveBrew(domain)
    }
}