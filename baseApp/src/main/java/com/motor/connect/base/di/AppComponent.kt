package com.motor.connect.base.di

import android.content.Context
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    AndroidInjectionModule::class
])
interface AppComponent {

    fun context(): Context
}