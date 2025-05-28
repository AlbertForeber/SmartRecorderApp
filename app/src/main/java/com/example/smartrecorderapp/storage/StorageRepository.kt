package com.example.smartrecorderapp.storage

import com.example.smartrecorderapp.realtime_database.DataRequest
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StorageRepository {
    suspend fun getFile( request: DataRequest ): Result<String>
    suspend fun removeFile( request: DataRequest ): Result<String>
    suspend fun addFile( request: DataRequest ): Result<String>
}