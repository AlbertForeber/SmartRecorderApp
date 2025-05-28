package com.example.smartrecorderapp.storage

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.example.smartrecorderapp.realtime_database.DataRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import jakarta.inject.Inject
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

class StorageRepositoryImpl @Inject constructor(
    storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : StorageRepository {

    private fun StorageReference.getReference(request: DataRequest ): StorageReference? {
        val uid = auth.uid ?: return null
        return this
            .child("users")
            .child(uid)
            .child(request.date)
            .child(request.lessonId.toString())
    }

    private val ref = storage.reference

    override suspend fun getFile(request: DataRequest): Result<String> {
        val ref = ref.getReference( request ) ?: return Result.failure(Exception("Not authorized"))

        try {
            if ( request.audioFile!!.exists() ) {
                return Result.success("Cache")
            }

            ref.getFile(request.audioFile)
            return Result.success("Storage")
        }
        catch( e: Exception ) {
            return Result.failure(e)
        }

    }

    override suspend fun removeFile(request: DataRequest): Result<String> {
        val ref = ref.getReference( request ) ?: return Result.failure(Exception("Not authorized"))

        try {
            ref.delete()
            return Result.success("Removed")
        }

        catch ( e: Exception ) {
            return Result.failure(e)
        }

    }

    override suspend fun addFile(request: DataRequest): Result<String> {
        val ref = ref.getReference( request ) ?: return Result.failure(Exception("Not authorized"))

        try {
            ref.putFile(request.audioFile!!.toUri())
            return Result.success("Uploaded")
        }

        catch ( e: Exception ) {
            return Result.failure(e)
        }

    }
}