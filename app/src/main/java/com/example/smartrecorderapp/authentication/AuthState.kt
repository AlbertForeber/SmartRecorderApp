package com.example.smartrecorderapp.authentication

import com.google.firebase.auth.FirebaseUser

/*
sealed class здесь — более естественный выбор, потому что:
Состояния AuthState — это часть одной доменной модели.
Нужна комбинация object и data class. Возможность добавить общую логику в родительский класс.
sealed interface оставьте для случаев, когда класс должен реализовывать несколько закрытых иерархий
(например, AuthState + AnalyticsEvent).
 */
sealed class AuthState {
    data object Loading: AuthState()
    data object Idle: AuthState()
    data class LoggedIn( val user: FirebaseUser ): AuthState()
    data class Error( val errorMessage: String? ): AuthState()
}