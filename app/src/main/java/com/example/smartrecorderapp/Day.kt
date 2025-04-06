package com.example.smartrecorderapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "day", primaryKeys = ["id", "lesson_id"])
class Day(
    val id: Int,
    @ColumnInfo(name = "lesson_id")
    val lessonId: Int,
    val lessons: String
)