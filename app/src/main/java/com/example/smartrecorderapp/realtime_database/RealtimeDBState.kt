package com.example.smartrecorderapp.realtime_database

import com.google.firebase.database.DataSnapshot

sealed interface RealtimeDBState {
    data class Idle( val transcription: String? ): RealtimeDBState
    data object InProgress: RealtimeDBState
    data object None: RealtimeDBState
    data class Error( val errorMessage: String? ): RealtimeDBState
}