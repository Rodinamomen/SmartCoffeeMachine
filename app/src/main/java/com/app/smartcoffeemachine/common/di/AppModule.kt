package com.app.smartcoffeemachine.common.di

import androidx.room.Room
import com.app.smartcoffeemachine.android.controller.BrewingServiceController
import com.app.smartcoffeemachine.common.data.repo.local.database.AppDatabase
import com.app.smartcoffeemachine.common.data.repo.remote.RemoteDataSourceProvider
import com.app.smartcoffeemachine.common.data.repo.remote.provideHttpClient
import com.app.smartcoffeemachine.common.domain.repo.remote.IRemoteDataSourceProvider
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
            encodeDefaults = true
        }
    }
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "smart_coffee_machine_database"
        ).build()
    }
    single {
        RemoteDataSourceProvider(client = provideHttpClient(), json = get())
    } bind IRemoteDataSourceProvider::class
    single {
        BrewingServiceController(
            context = androidContext()
        )
    }
    includes(featuresModule, loggerModule)
}