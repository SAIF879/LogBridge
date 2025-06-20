package com.example.logbridge

import android.app.Application
import com.example.logbridge.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.context.startKoin

class LogBridgeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@LogBridgeApplication)
           // modules(appModule)
        }
    }
}