package com.motor.connect.base.app

import android.app.Application
import com.motor.connect.base.di.AppModule
import com.motor.connect.base.di.DaggerAppComponent
import com.orhanobut.hawk.HawkBuilder

class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        initSharePreference()
        initDagger()
    }

    private fun initDagger() {
        DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()
    }

    private fun initSharePreference() {
        HawkBuilder(applicationContext)
                .build()

    }

}