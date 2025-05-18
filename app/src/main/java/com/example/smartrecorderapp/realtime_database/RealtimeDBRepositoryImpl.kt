package com.example.smartrecorderapp.realtime_database

import android.util.Log
import androidx.room.Database
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RealtimeDBRepositoryImpl @Inject constructor(
    database: FirebaseDatabase,
    val auth: FirebaseAuth
) : RealtimeDBRepository {

    private val ref = database.reference

    fun DatabaseReference.getReference( request: RealtimeDBRequest ): DatabaseReference {
        return this
            .child("users")
            .child( auth.uid!! )
            .child(request.date)
            .child(request.lessonId.toString())
    }

    override suspend fun getLesson( request: RealtimeDBRequest ): Result<DataSnapshot?> {
        return try {
            val reference = ref.getReference( request )
            Result.success( reference.get().await() )
        }
        catch (e: Exception) {
            Result.failure( e )
        }
    }

    override suspend fun addLesson( request: RealtimeDBRequest ): Result<String> {
        return try {
            val generativeModel = Firebase.vertexAI.generativeModel("gemini-2.0-flash")
            val prompt = content {
                text("Транскрибируй это аудио")
                inlineData(
                    request.audioFile!!.readBytes(),
                    mimeType = "audio/mp3"
                )
            }
            val response =
                generativeModel.generateContent(prompt)
            val reference = ref.getReference( request )
            reference.setValue( response.text ).await()
            Result.success( response.text!! )
        }
        catch (e: Exception) {
            Result.failure( e )
        }
    }

    override suspend fun removeLesson( request: RealtimeDBRequest ): Result<String> {
        return try {
            val reference = ref.getReference( request )
            reference.removeValue().await()
            Result.success("")
        }
        catch (e: Exception) {
            Result.failure( e )
        }
    }


}