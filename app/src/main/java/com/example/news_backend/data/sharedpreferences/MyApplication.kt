package com.example.news_backend.data.sharedpreferences

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DataLocalManager.init(applicationContext)
    }
}