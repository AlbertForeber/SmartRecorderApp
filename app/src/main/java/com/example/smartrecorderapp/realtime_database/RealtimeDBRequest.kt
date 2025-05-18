package com.example.smartrecorderapp.realtime_database

import java.io.File

data class RealtimeDBRequest(
    val date: String,
    val lessonId: Long,
    val audioFile: File? = null
)
