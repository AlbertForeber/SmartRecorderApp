package com.example.smartrecorderapp

import androidx.room.TypeConverter

class ListConverter {
    @TypeConverter
    fun listToString(value: MutableList<String>): String {
        var finalString = ""
        value.forEach {
            finalString += "$it/"
        }
        return finalString
    }

    @TypeConverter
    fun stringToList(value: String): MutableList<String> {
        return value.split('/').toMutableList()
    }
}