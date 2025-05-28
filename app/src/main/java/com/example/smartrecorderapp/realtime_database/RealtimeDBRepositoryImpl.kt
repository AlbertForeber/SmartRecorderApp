package com.example.smartrecorderapp.realtime_database

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RealtimeDBRepositoryImpl @Inject constructor(
    database: FirebaseDatabase,
    val auth: FirebaseAuth
) : RealtimeDBRepository {

    private val ref = database.reference

    private fun DatabaseReference.getReference(request: DataRequest ): DatabaseReference? {
        val uid = auth.uid ?: return null
        return this
            .child("users")
            .child( uid )
            .child(request.date)
            .child(request.lessonId.toString())
    }

    override suspend fun getLesson( request: DataRequest ): Result<DataSnapshot?> {
        val reference = ref.getReference( request ) ?: return Result.failure( Exception("Not authorized") )
        return try {
            Result.success( reference.get().await() )
        }
        catch (e: Exception) {
            Result.failure( e )
        }
    }


    // Добавление реактивности (наблюдение за состоянием) - не используется здесь,
    // используется в StorageRepo
    /*
    Реактивность в коде — это парадигма программирования, при которой приложение автоматически
    реагирует на изменения данных, обновляя только те части системы, которые зависят от этих
    изменений.
     */
    override suspend fun observeLesson(request: DataRequest): Flow<DataSnapshot?> = callbackFlow {
        // Проверка на авторизацию, закрытие потока при исключении
        val reference = ref.getReference(request) ?: run {
            close()
            return@callbackFlow
        }

        val listener = reference.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        } )

        awaitClose {
            reference.removeEventListener(listener)
        }
    }


    override suspend fun addLesson( request: DataRequest ): Result<String> {
        val reference = ref.getReference( request ) ?: return Result.failure( Exception("Not authorized") )
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
            reference.setValue( response.text ).await()
            Result.success( response.text!! )
        }
        catch (e: Exception) {
            Result.failure( e )
        }
    }

    override suspend fun removeLesson( request: DataRequest ): Result<String> {
        val reference = ref.getReference( request ) ?: return Result.failure( Exception("Not authorized") )
        return try {
            reference.removeValue().await()
            Result.success("")
        }
        catch (e: Exception) {
            Result.failure( e )
        }
    }


}