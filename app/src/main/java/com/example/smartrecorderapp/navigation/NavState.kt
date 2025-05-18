package com.example.smartrecorderapp.navigation

sealed class NavState {
    data object Home : NavState()
    data object Auth : NavState()
    data object Lesson : NavState()
}