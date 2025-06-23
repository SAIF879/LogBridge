package com.example.logbridge

import android.app.Application
import com.example.logbridge.di.appModule
import com.example.logbridge.utils.others.ObjectBoxBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import timber.log.Timber


class LogBridgeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBoxBuilder.init(this)
        GlobalContext.startKoin {
            androidContext(this@LogBridgeApplication)
            modules(appModule)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}