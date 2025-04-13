package com.example.smartrecorderapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartrecorderapp.database.Day
import com.example.smartrecorderapp.database.ViewModelLDB
import com.example.smartrecorderapp.date_functional.SelectData
import com.example.smartrecorderapp.date_functional.formatDate
import com.example.smartrecorderapp.screens.AuthScreen
import com.example.smartrecorderapp.screens.LessonScreen
import com.example.smartrecorderapp.screens.MainScreen
import com.example.smartrecorderapp.topbars.LessonScreenTopBar
import com.example.smartrecorderapp.topbars.MainScreenTopBar
import com.example.smartrecorderapp.ui.theme.SmartRecorderAppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var lessonLDB: ViewModelLDB

    @SuppressLint("MutableCollectionMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartRecorderAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry.value?.destination?.route
                val auth = Firebase.auth
                var home = "home"

                var currentUser = auth.currentUser
                if (currentUser == null ) {
                    home = "auth"
                }

                val provider = ViewModelProvider(this)
                lessonLDB = provider[ViewModelLDB::class]
                Scaffold(
                    topBar = {
//                        if (currentRoute == "home") {
//                            MainScreenTopBar(lessonLDB)
//                        }
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
                    NavHost(
                        navController = navController,
                        startDestination = home,
                    ) {
                        composable(
                            "home",
                            enterTransition = {
                                fadeIn(
                                    animationSpec = tween(
                                        150, easing = LinearEasing
                                    )
                                ) + slideIntoContainer(
                                    animationSpec = tween(150, easing = EaseIn),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            },
                            exitTransition = {
                                fadeOut(
                                    animationSpec = tween(
                                        150, easing = LinearEasing
                                    )
                                ) + slideOutOfContainer(
                                    animationSpec = tween(150, easing = EaseOut),
                                    towards = AnimatedContentTransitionScope.SlideDirection.End
                                )
                            }
                        ) {

                            MainScreen(navController, lessonLDB, innerPadding)
                        }

                        composable("lesson") {
                            LessonScreen(navController, lessonLDB, innerPadding)
                        }

                        composable(
                            "auth",
                            exitTransition = {
                                fadeOut(
                                    animationSpec = tween(
                                        150, easing = LinearEasing
                                    )
                                ) + slideOutOfContainer(
                                    animationSpec = tween(150, easing = EaseOut),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            }

                        ) {
                            AuthScreen(auth, navController, { currentUser = it }, this@MainActivity)
                        }
                    }
                }
            }
        }
    }
}