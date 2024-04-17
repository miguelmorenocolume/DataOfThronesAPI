package com.example.dataofthronesapi.dependencies

import android.app.Application

class DataOfThrones : Application() {
    private lateinit var _appContainer: AppContainer
    val appcontainer get() = _appContainer

    override fun onCreate() {
        super.onCreate()
        _appContainer = AppContainer(this)
    }
}