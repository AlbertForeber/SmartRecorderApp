package com.example.smartrecorderapp.viewmodels

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.example.smartrecorderapp.audio_processing.AndroidAudioPlayer
import com.example.smartrecorderapp.database.LessonFDB
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@HiltViewModel
class FirebaseStorageViewModel @Inject constructor(
    firebaseStorage: FirebaseStorage,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ViewModel( ) {
    private val reference = firebaseStorage.reference
    private lateinit var currentProcess: Job
    fun addLesson( dateInMillis: String, lessonId: Long, file: File) {

        currentProcess = viewModelScope.launch( Dispatchers.IO ) {
            reference
                .child("users")
                .child("${auth.currentUser?.uid}")
                .child(dateInMillis)
                .child("$lessonId")
                .putFile(file.toUri())
        }
    }

    fun getLesson(dateInMillis: String, lessonId: Long, destinationFile: File ) {
        currentProcess = viewModelScope.launch(Dispatchers.IO) {
            val file: File = destinationFile
            reference
                .child("users")
                .child("${auth.currentUser?.uid}")
                .child(dateInMillis)
                .child("$lessonId")
                .getFile(file)
                .addOnSuccessListener {
                    Log.i("DEBUGGE", "Successfully got file")
                }
                .addOnFailureListener{
                    Log.i("DEBUGGE", "Error while getting file. Getting in users/${auth.currentUser?.uid}/$dateInMillis/$lessonId")
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
                .delete()
        }
    }
    override fun onCleared() {
        super.onCleared()
        Log.i("DEBUGGE", "Storage process canceled")
    }
}