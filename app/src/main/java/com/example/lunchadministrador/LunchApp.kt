package com.example.lunchadministrador

import android.app.Application
import com.example.lunchadministrador.di.AppContainer

class LunchApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
