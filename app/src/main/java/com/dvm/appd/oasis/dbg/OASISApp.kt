package com.dvm.appd.oasis.dbg

import android.app.Application
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.dvm.appd.oasis.dbg.di.AppComponent
import com.dvm.appd.oasis.dbg.di.AppModule
import com.dvm.appd.oasis.dbg.di.DaggerAppComponent
import io.reactivex.plugins.RxJavaPlugins

class OASISApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        RxJavaPlugins.setErrorHandler {
            Log.e("App", "error $it: ${it.message ?: "No message"}")
            // Crashlytics.log("Global Event handler Crash With error = ${it.toString()}")
        }

    }
}