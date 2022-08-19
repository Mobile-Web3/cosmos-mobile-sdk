package com.mobileweb3.cosmossdk.android

import android.app.Application
import com.mobileweb3.cosmossdk.app.MainStore
import com.mobileweb3.cosmossdk.core.create
import com.mobileweb3.cosmossdk.interactor.MainInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class App : Application() {

    private val appModule = module {
        single { MainInteractor.create(get(), BuildConfig.DEBUG) }
        single { MainStore(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)

            androidContext(this@App)
            modules(appModule)
        }
    }
}