package com.example.smartrecorderapp.storage

sealed interface StorageState {
    data class Idle( val from: String? ): StorageState
    data class Error( val errorMessage: String? ): StorageState
    data object InProgress: StorageState
    data object None: StorageState
}