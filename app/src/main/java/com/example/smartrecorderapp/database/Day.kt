package com.example.smartrecorderapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "day", primaryKeys = ["id", "lesson_id"])
class Day(
    val id: Int,
    @ColumnInfo(name = "lesson_id")
    val lessonId: Int,
    val lessons: String
)