package com.example.smartrecorderapp.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class LessonFDB(
    val lessonId: Int? = null,
    val pathInStorage: String? = null,
    val transcription: String? = null
)