package com.example.smartrecorderapp.date_functional

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class StartDateReference @Inject constructor(
    @ApplicationContext context: Context
) {
    private val _sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    var startDate: Long?
        get() {
            return _sharedPref.getLong("start_date", 0L).takeIf { it > 0L && it <= System.currentTimeMillis() }
        }
        set(value) {
            Log.i("DEBUGGE", "set startDateInMillis: $value")
            _sharedPref.edit { putLong("start_date", value ?: 0L )
            } }
    init {
        Log.i("DEBUGGE", "intialized startDateInMillis")
    }
}