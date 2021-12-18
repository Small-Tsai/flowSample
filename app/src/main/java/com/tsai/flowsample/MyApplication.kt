package com.tsai.flowsample

import android.app.Application
import com.tsai.flowsample.data.source.Repo

class MyApplication : Application() {

    val repo: Repo
        get() = ServiceLocator.provideRepo()

    companion object {
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}