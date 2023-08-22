package com.example.pocketmap.app

import android.app.Application
import com.example.pocketmap.di.dataModule
import com.example.pocketmap.di.domainModule
import com.example.pocketmap.di.presentationModule
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(com.example.pocketmap.BuildConfig.MAPKIT_API_KEY)

        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(listOf(dataModule, domainModule, presentationModule))
        }
    }
}