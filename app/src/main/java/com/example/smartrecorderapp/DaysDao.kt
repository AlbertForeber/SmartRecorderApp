package com.example.smartrecorderapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
@Dao
interface DaysDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDay(day: Day)
    @Query("SELECT * FROM Day WHERE id == :id")
    suspend fun getDay(id: Int): Day
    @Query("DELETE FROM Day WHERE id == :id")
    fun deleteDayByID(id: Int)
    @Query ("SELECT * FROM Day")
    fun getDays(): LiveData<List<Day>>
}