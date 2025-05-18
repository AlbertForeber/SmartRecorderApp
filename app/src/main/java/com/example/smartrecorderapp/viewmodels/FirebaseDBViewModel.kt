package com.example.smartrecorderapp.viewmodels

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.smartrecorderapp.authentication.AuthRepository
import com.example.smartrecorderapp.authentication.AuthState
import com.example.smartrecorderapp.database.LessonFDB
import com.example.smartrecorderapp.database.LessonsDB
import com.example.smartrecorderapp.realtime_database.RealtimeDBRepository
import com.example.smartrecorderapp.realtime_database.RealtimeDBRequest
import com.example.smartrecorderapp.realtime_database.RealtimeDBState
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

@HiltViewModel
class FirebaseDBViewModel @Inject constructor(
    private val realtimeDBRepository: RealtimeDBRepository,
) : ViewModel( ) {

    private var _dbState =  MutableStateFlow<RealtimeDBState>(RealtimeDBState.None)

    var dbState = _dbState.asStateFlow()

    fun addLesson(  date: String,
                    lessonId: Long,
                    audioFile: File? ) {

        viewModelScope.launch( Dispatchers.IO ) {
            val result = realtimeDBRepository.addLesson(
                RealtimeDBRequest(
                    date = date,
                    lessonId = lessonId,
                    audioFile = audioFile
                ) )
            result.fold(
                onSuccess = { _dbState.value = RealtimeDBState.Idle( it ) },
                onFailure = { _dbState.value = RealtimeDBState.Error( it.message ) }
            )
        }
    }

    fun getLesson( date: String,
                   lessonId: Long ) {
        viewModelScope.launch( Dispatchers.IO ) {
            val result = realtimeDBRepository.getLesson(
                RealtimeDBRequest(
                    date = date,
                    lessonId = lessonId
                ) )
            result.fold(
                onSuccess = {
                    var result = it?.value.toString()
                    if ( result == "null" ) _dbState.value = RealtimeDBState.Error("File's not found")
                    _dbState.value = RealtimeDBState.Idle( result )
                            },
                onFailure = { _dbState.value = RealtimeDBState.Error( it.message ) }
            )
        }
    }


    fun removeLesson( date: String,
                      lessonId: Long ) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = realtimeDBRepository.removeLesson(
                RealtimeDBRequest(
                    date = date,
                    lessonId = lessonId
                ) )
            result.fold(
                onSuccess = { _dbState.value = RealtimeDBState.None },
                onFailure = { _dbState.value = RealtimeDBState.Error(it.message) }
            )
        }
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("DEBUGGE", "DB process canceled")
    }
}