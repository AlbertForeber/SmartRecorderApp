package com.example.smartrecorderapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {
    fun login(
        email: String,
        password: String,
        onSuccess: ( FirebaseUser? ) -> Unit,
        onFailure: () -> Unit,
        navController: NavController
    ) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    viewModelScope.launch( Dispatchers.IO ) {
                        if (task.isSuccessful) {
                            onSuccess(auth.currentUser)
                            withContext(Dispatchers.Main ) {
                                navController.navigate("home") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            }
                        }
                        else {
                            withContext( Dispatchers.Main ) {
                                onFailure()
                            }
                        }
                    }
                }
        }
    }

    fun register(
        email: String,
        password: String,
        onSuccess: ( FirebaseUser? ) -> Unit,
        onFailure: () -> Unit,
        navController: NavController
    ) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    viewModelScope.launch( Dispatchers.IO ) {
                        if (task.isSuccessful) {
                            onSuccess( auth.currentUser )
                            delay(100)
                            withContext( Dispatchers.Main ) {
                                navController.navigate("home") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            }
                        }
                        else {
                            withContext( Dispatchers.Main ) {
                                onFailure()
                            }
                        }
                    }
                }
        }
    }
}