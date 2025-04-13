package com.example.smartrecorderapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaysDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDay(day: Day)
    @Update
    fun updateDay(id: Day)
    @Query("SELECT * FROM Day WHERE id == :id")
    suspend fun getDay(id: Int): List<Day>
    @Query("DELETE FROM Day WHERE id == :id AND lesson_id == :lessonId")
    fun deleteLessonByID(id: Int, lessonId: Int)
}