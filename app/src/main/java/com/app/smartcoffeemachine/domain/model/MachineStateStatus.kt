package com.app.smartcoffeemachine.domain.model

enum class MachineStateStatus {
    IDLE,
    HEATING,
    READY,
    BREWING,
    ERROR
}