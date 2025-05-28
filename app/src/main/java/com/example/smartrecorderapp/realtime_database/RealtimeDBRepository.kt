package com.example.smartrecorderapp.realtime_database

import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface RealtimeDBRepository {
    suspend fun getLesson( request: DataRequest ): Result<DataSnapshot?>
    suspend fun observeLesson( request: DataRequest ): Flow<DataSnapshot?>
    suspend fun addLesson( request: DataRequest ): Result<String>
    suspend fun removeLesson( request: DataRequest ): Result<String>
}