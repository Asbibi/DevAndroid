package com.andrealouis.devmobile.main

import android.app.Application
import com.andrealouis.devmobile.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.INSTANCE = Api(this)
    }
}