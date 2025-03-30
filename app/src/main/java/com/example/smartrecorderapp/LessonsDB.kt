package com.example.smartrecorderapp

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Day::class], version = 1)
abstract class LessonsDB : RoomDatabase() {
    abstract fun daysDao(): DaysDao

    companion object {
        var INSTANCE: LessonsDB? = null

        fun getLessonDB(context: Context): LessonsDB? {
            if (INSTANCE == null) {
                synchronized(LessonsDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LessonsDB::class.java,
                        "LessonsDB.db"
                    ).build()
                }
                Log.i("DB_Creator", "DB Created!")
            }
            return INSTANCE
        }

        fun destroyDB() {
            INSTANCE = null
        }
    }
}