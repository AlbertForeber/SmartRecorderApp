package com.example.smartrecorderapp.authentication

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

// Отделили использование от реализации
// (хорошая практика при использовании Dependency Injection с @Bind)
interface AuthRepository {
    val authState: Flow<FirebaseUser?>
    suspend fun logIn( request: AuthRequest ): Result<FirebaseUser>
    suspend fun register( request: AuthRequest ): Result<FirebaseUser>
    suspend fun logOut()
}