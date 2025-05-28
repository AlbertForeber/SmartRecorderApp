package com.example.smartrecorderapp.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotApplyResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.smartrecorderapp.authentication.AuthRepository
import com.example.smartrecorderapp.authentication.AuthRequest
import com.example.smartrecorderapp.authentication.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val authRepository: AuthRepository,
) : ViewModel() {


    /*
    Используется, чтобы спрятать MutableStateFlow от внешнего слоя (UI).
    UI видит только StateFlow, и не может менять его напрямую — только ViewModel управляет
    изменениями. Это >инкапсуляция<.

    Делаем через StateFlow для нормальной работой с ассинхроностью - хороший паттерн.

    Отличие от обычного Flow - есть collectAsState - для преобразования в метку для хранения
    состояния UI уже в самом Composable + горячий поток (имеет значение всегда, когда обычный
    начинает работу лишь после первой подписки)
     */
    private var _authState = MutableStateFlow<AuthState>(AuthState.Loading)

    init {
        Log.i("DEBUGGE", "Auth VM created")
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.authState.collect { state ->
                if ( state != null ) _authState.value = AuthState.LoggedIn( state )
                else _authState.value = AuthState.Idle
            }
        }
    }

    var authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(
        email: String,
        password: String,
    ) {

        // Проверка на изначальную валидность данных - архитетурно неправильно запускать корутину
        // если изначальные данные уже неверны
        if (email.isBlank() && password.isBlank()) { // Проверка что в строке есть символы помимо пробелов
            _authState.value = AuthState.Error("Fields should not be empty")
        } else {
            viewModelScope.launch( Dispatchers.IO ) {
                _authState.value = AuthState.Loading
                val result = authRepository.logIn(
                    AuthRequest(
                        email.trim(),
                        password
                    )
                ) // trim - удаление пробелов
                result.fold(
                    onSuccess = {
                        _authState.value = AuthState.LoggedIn(it) },
                    onFailure = {
                        _authState.value = AuthState.Error(it.message ?: "Unknown error")
                    }
                )
            }
        }
    }

    fun register(
        email: String,
        password: String,
    ) {
        if (email.isBlank() && password.isBlank()) { // Проверка что в строке есть символы помимо пробелов
            _authState.value = AuthState.Error("Fields should not be empty")
        } else {
            viewModelScope.launch( Dispatchers.IO ) {
                val result = authRepository.register(
                    AuthRequest(
                        email.trim(),
                        password
                    )
                ) // trim - удаление пробелов
                result.fold(
                    onSuccess = { _authState.value = AuthState.LoggedIn(it) },
                    onFailure = {
                        _authState.value = AuthState.Error(it.message ?: "Unknown error")
                    }
                )
            }
        }
    }
}