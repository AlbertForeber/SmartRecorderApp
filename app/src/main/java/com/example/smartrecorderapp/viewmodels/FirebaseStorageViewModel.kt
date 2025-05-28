package com.example.smartrecorderapp.viewmodels

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.example.smartrecorderapp.audio_processing.AndroidAudioPlayer
import com.example.smartrecorderapp.database.LessonFDB
import com.example.smartrecorderapp.realtime_database.DataRequest
import com.example.smartrecorderapp.storage.StorageRepository
import com.example.smartrecorderapp.storage.StorageState
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

@HiltViewModel
class FirebaseStorageViewModel @Inject constructor(
    private val firebaseRepository: StorageRepository,
    private val auth: FirebaseAuth,
) : ViewModel( ) {

    private var _storageState = MutableStateFlow<StorageState>(StorageState.None)
    var storageState: StateFlow<StorageState> = _storageState.asStateFlow()

    fun addLesson( dateInMillis: String, lessonId: Long, file: File) {
        viewModelScope.launch( Dispatchers.IO ) {
            _storageState.value = StorageState.InProgress

            val result = firebaseRepository.addFile(
                DataRequest(
                    date = dateInMillis,
                    lessonId = lessonId,
                    audioFile = file
                )
            )

            result.fold(
                onSuccess = { _storageState.value = StorageState.Idle( it ) },
                onFailure = { _storageState.value = StorageState.Error( it.message ) }
            )
        }
    }

    fun getLesson(dateInMillis: String, lessonId: Long, destinationFile: File ) {
        viewModelScope.launch( Dispatchers.IO ) {
            _storageState.value = StorageState.InProgress
            val result = firebaseRepository.getFile(
                DataRequest(
                    date = dateInMillis,
                    lessonId = lessonId,
                    audioFile = destinationFile
                )
            )

            result.fold(
                onSuccess = { _storageState.value = StorageState.Idle( it ) },
                onFailure = { _storageState.value = StorageState.Error( it.message ) }
            )
        }
    }

    fun removeLesson( dateInMillis: String, lessonId: Long ) {
        viewModelScope.launch( Dispatchers.IO ) {
            _storageState.value = StorageState.InProgress
            val result = firebaseRepository.removeFile(
                DataRequest(
                    date = dateInMillis,
                    lessonId = lessonId
                )
            )

            result.fold(
                onSuccess = { _storageState.value = StorageState.None },
                onFailure = { _storageState.value = StorageState.Error( it.message ) }
            )
        }
    }
    override fun onCleared() {
        super.onCleared()
        Log.i("DEBUGGE", "Storage process canceled")
    }
}