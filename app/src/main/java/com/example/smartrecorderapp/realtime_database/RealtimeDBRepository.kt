package com.example.smartrecorderapp.realtime_database

import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface RealtimeDBRepository {
    suspend fun getLesson( request: RealtimeDBRequest ): Result<DataSnapshot?>
    suspend fun addLesson( request: RealtimeDBRequest ): Result<String>
    suspend fun removeLesson( request: RealtimeDBRequest ): Result<String>
}