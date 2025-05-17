package com.example.smartrecorderapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.smartrecorderapp.database.LessonFDB
import com.example.smartrecorderapp.database.LessonsDB
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

@HiltViewModel
class FirebaseDBViewModel @Inject constructor(
    firebaseDatabase: FirebaseDatabase,
    private val auth: FirebaseAuth
) : ViewModel( ) {
    private lateinit var currentProcess: Job
    private val reference = firebaseDatabase.reference
    fun addLesson( dateInMillis: String, lessonId: Long, audioFile: File, onSuccess: () -> Unit) {
        currentProcess = viewModelScope.launch( Dispatchers.IO ) {
            val generativeModel = Firebase.vertexAI.generativeModel("gemini-2.0-flash")
            val prompt = content {
                text("Транскрибируй это аудио")
                inlineData(
                    audioFile.readBytes(),
                    mimeType = "audio/mp3"
                )
            }
            val response =
                generativeModel.generateContent(prompt)
            reference
                .child("users")
                .child("${auth.currentUser?.uid}")
                .child(dateInMillis)
                .child("$lessonId")
                .setValue(response.text)
                .addOnSuccessListener {
                    onSuccess()
                }
        }
    }

    fun getLesson( dateInMillis: String, lessonId: Long, callback: ( String? ) -> Unit ) {
        currentProcess = viewModelScope.launch( Dispatchers.IO ) {
            reference
                .child("users")
                .child("${auth.currentUser?.uid}")
                .child(dateInMillis)
                .child("$lessonId")
                .get()
                .addOnSuccessListener {
                    callback(it.value.toString())
                }
                .addOnFailureListener {
                    callback( null )
                }
        }
    }


    fun removeLesson( dateInMillis: String, lessonId: Long ) {
        currentProcess = viewModelScope.launch( Dispatchers.IO ) {
            reference
                .child("users")
                .child("${auth.currentUser?.uid}")
                .child(dateInMillis)
                .child("$lessonId")
                .removeValue()
        }
    }

    suspend fun df ( )  {

    }

    override fun onCleared() {
        super.onCleared()
        Log.i("DEBUGGE", "DB process canceled")
        currentProcess.cancel()
    }
}