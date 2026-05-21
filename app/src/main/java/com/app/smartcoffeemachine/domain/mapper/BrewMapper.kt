package com.app.smartcoffeemachine.domain.mapper

import com.app.smartcoffeemachine.data.model.entity.BrewEntity
import com.app.smartcoffeemachine.domain.model.Brew

fun Brew.toEntity() = BrewEntity(
    id = 0,
    brewType = this.brewType.name,
    status = this.status.name,
    timestamp = this.timestamp
)