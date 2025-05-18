package com.example.smartrecorderapp.navigation

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.example.smartrecorderapp.MainActivity
import com.example.smartrecorderapp.authentication.AuthState
import com.example.smartrecorderapp.screens.AuthScreen
import com.example.smartrecorderapp.screens.LessonScreen
import com.example.smartrecorderapp.screens.MainScreen
import com.example.smartrecorderapp.viewmodels.AuthViewModel
import com.example.smartrecorderapp.viewmodels.ViewModelLDB
import kotlin.getValue

@Composable
fun NavComposable( innerPadding: PaddingValues,
                   navController: NavHostController,
                   home: String,
                   authViewModel: AuthViewModel = hiltViewModel(),
                   lessonLDB: ViewModelLDB = hiltViewModel()
) {

    NavHost(
        navController = navController,
        startDestination = home,
    ) {
        Log.i("DEBUGGE", "NavHost created")
        navigation(startDestination = "home", route = "main") {
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
            AuthScreen( navController, authViewModel )
        }
    }
}