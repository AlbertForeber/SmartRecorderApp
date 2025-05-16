package com.example.smartrecorderapp.dependency_injection

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import android.util.Log

class AppObserver( private val context: Context) : DefaultLifecycleObserver {
    override fun onStop(owner: LifecycleOwner) {
        Log.i("DEBUGGE", "Cleared memory")
        context.cacheDir.deleteRecursively()
    }
}