package com.app.smartcoffeemachine.common.di

import com.app.smartcoffeemachine.data.repo.local.MachineLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File
private const val MACHINE_LOG_FILE = "state_machine_log.txt"

val loggerModule = module {

    single<File>(named(MACHINE_LOG_FILE)) {
        File(
            androidContext().filesDir,
            MACHINE_LOG_FILE
        )
    }
    single<MachineLogger> { MachineLogger(logFile = get(named(MACHINE_LOG_FILE)),)
    }
}