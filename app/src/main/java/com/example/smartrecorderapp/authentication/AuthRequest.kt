package com.example.smartrecorderapp.authentication

// Использование AuthRequest как DTO — хороший тон.
data class AuthRequest(
    val email: String,
    val password: String
)
