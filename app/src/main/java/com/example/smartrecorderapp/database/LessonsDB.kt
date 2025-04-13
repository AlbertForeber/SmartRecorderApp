package com.example.smartrecorderapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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

            }
            return INSTANCE
        }

        fun destroyDB() {
            INSTANCE = null
        }
    }
}