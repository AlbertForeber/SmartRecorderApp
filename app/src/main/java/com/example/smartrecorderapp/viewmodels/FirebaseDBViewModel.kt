package com.example.smartrecorderapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecorderapp.realtime_database.RealtimeDBRepository
import com.example.smartrecorderapp.realtime_database.DataRequest
import com.example.smartrecorderapp.realtime_database.RealtimeDBState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
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
            _dbState.value = RealtimeDBState.InProgress
            val result = realtimeDBRepository.addLesson(
                DataRequest(
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
            Log.i("DEBUGGE", "getLesson ViewModel launched")
            _dbState.value = RealtimeDBState.InProgress
            val result = realtimeDBRepository.getLesson(
                DataRequest(
                    date = date,
                    lessonId = lessonId
                ) )
            result.fold(
                onSuccess = {
                    val res = it?.value.toString()
                    if ( res == "null" ) {
                        _dbState.value = RealtimeDBState.Error("Запись не обнаружена")
                    }
                    else _dbState.value = RealtimeDBState.Idle( res )
                            },
                onFailure = { _dbState.value = RealtimeDBState.Error( it.message ) }
            )
        }
    }



    fun removeLesson( date: String,
                      lessonId: Long ) {
        viewModelScope.launch(Dispatchers.IO) {
            _dbState.value = RealtimeDBState.InProgress
            val result = realtimeDBRepository.removeLesson(
                DataRequest(
                    date = date,
                    lessonId = lessonId
                ) )
            result.fold(
                onSuccess = { _dbState.value = RealtimeDBState.Error("Запись удалена") },
                onFailure = { _dbState.value = RealtimeDBState.Error(it.message) }
            )
        }
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("DEBUGGE", "DB process canceled")
    }
}