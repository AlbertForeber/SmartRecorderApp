package com.example.smartrecorderapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartrecorderapp.authentication.AuthState
import com.example.smartrecorderapp.database.Day
import com.example.smartrecorderapp.viewmodels.ViewModelLDB
import com.example.smartrecorderapp.date_functional.SelectData
import com.example.smartrecorderapp.date_functional.formatDate
import com.example.smartrecorderapp.navigation.NavComposable
import com.example.smartrecorderapp.screens.AuthScreen
import com.example.smartrecorderapp.screens.LessonScreen
import com.example.smartrecorderapp.screens.MainScreen
import com.example.smartrecorderapp.topbars.LessonScreenTopBar
import com.example.smartrecorderapp.topbars.MainScreenTopBar
import com.example.smartrecorderapp.ui.theme.SmartRecorderAppTheme
import com.example.smartrecorderapp.viewmodels.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    @Inject lateinit var auth: FirebaseAuth

    @SuppressLint("MutableCollectionMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartRecorderAppTheme {
                val lessonLDB: ViewModelLDB = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
                val authViewModel: AuthViewModel = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)

                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry.value?.destination?.route

                var currentUser = auth.currentUser

                val authState by authViewModel.authState.collectAsState()

                Scaffold(
                    topBar = {
                        AnimatedVisibility(
                            currentRoute == "home",
                            enter = fadeIn(
                                animationSpec = tween(
                                    300, easing = LinearEasing
                                )
                            ) + expandHorizontally(),
                            exit = fadeOut(
                                animationSpec = tween(
                                    300, easing = LinearEasing
                                )
                            ) + shrinkHorizontally()
                        ) {
                            MainScreenTopBar(lessonLDB, auth, { currentUser = it }, navController)
                        }
                        AnimatedVisibility(
                            currentRoute == "lesson",
                            enter = fadeIn(
                                animationSpec = tween(
                                    300, easing = LinearEasing
                                )
                            ) + expandHorizontally(),
                            exit = fadeOut(
                                animationSpec = tween(
                                    300, easing = LinearEasing
                                )
                            ) + shrinkHorizontally()
                        ) {
                            LessonScreenTopBar(navController, lessonLDB)
                        }

                    },
                    floatingActionButton = {
                        if (currentRoute == "home") {
                            FloatingActionButton(
                                containerColor = MaterialTheme.colorScheme.surface,
                                onClick = {
                                    lessonLDB.isDialog.value = true

                                }
                            ) {
                                Icon(
                                    Icons.Filled.Add,
                                    "Кнопка добавления занятия",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                ) { innerPadding ->
                    val startDestination: String = when( authState ) {
                        is AuthState.Loading -> "loading"
                        is AuthState.Error -> "auth"
                        is AuthState.Idle -> "auth"
                        is AuthState.LoggedIn -> "main"
                    }


                    when (startDestination) {
                        "main", "auth" -> {
                            NavComposable(innerPadding, navController, startDestination)
                        }
                        "loading" -> Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                }
            }
        }
    }
}