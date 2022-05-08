package ru.gizmodo.ditest

import android.app.Application
import android.content.Context

class App : Application() {
    lateinit var appGraph: AppGraph

    companion object {
        private lateinit var appInstance: App
        fun instance(): App {
            return appInstance
        }

        fun applicationContext(): Context {
            return appInstance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        appGraph = DaggerAppGraph
            .builder()
            .appModule(ru.gizmodo.ditest.AppModule(this))
            .testModule(TestModule(this))
            .build()
    }
}