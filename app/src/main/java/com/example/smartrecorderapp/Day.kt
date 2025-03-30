package com.example.smartrecorderapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "day")
class Day(
    @PrimaryKey
    val id: Int,
    val lessons: String
)