package com.example.smartrecorderapp.dependency_injection

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmartRecorderApplication : Application( ) {
    override fun onCreate() {
        super.onCreate()
        val observer = AppObserver( this )
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
    }

}