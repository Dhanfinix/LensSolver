package com.dhandev.lenssolver

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dhandev.lenssolver.ui.theme.LensSolverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            LensSolverTheme {
                val safeGestureInset = WindowInsets.safeGestures.asPaddingValues()
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = safeGestureInset.calculateBottomPadding()),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = HomeDestination()
                    ){
                        composable<HomeDestination>(
                            enterTransition = {
                                fadeIn(
                                    animationSpec = tween(
                                        500
                                    )
                                ) + slideIntoContainer(
                                    animationSpec = tween(500, easing = EaseIn),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                                )
                            },
                            exitTransition = {
                                fadeOut(
                                    animationSpec = tween(
                                        500
                                    )
                                ) + slideOutOfContainer(
                                    animationSpec = tween(500, easing = EaseOut),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                                )
                            }
                        ) { backStackEntry->
                            val args = backStackEntry.toRoute<HomeDestination>()
                            val capturedUri = args.capturedUriString?.let { Uri.parse(it) }

                            HomeScreen(
                                modifier = Modifier.safeGesturesPadding().imePadding(),
                                takenPhotoUri = capturedUri
                            ){
                                navController.navigate(CameraDestination){
                                    popUpTo(navController.graph.findStartDestination().id) {}
                                    launchSingleTop = true
                                }
                            }
                        }

                        composable<CameraDestination>(
                            enterTransition = {
                                fadeIn(
                                    animationSpec = tween(
                                        500
                                    )
                                ) + slideIntoContainer(
                                    animationSpec = tween(500, easing = EaseIn),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                                )
                            },
                            exitTransition = {
                                fadeOut(
                                    animationSpec = tween(
                                        500
                                    )
                                ) + slideOutOfContainer(
                                    animationSpec = tween(500, easing = EaseOut),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                                )
                            }
                        ) {
                            CameraScreen(
                                modifier = Modifier.safeGesturesPadding(),
                                delegate = object : CameraDelegate {
                                override fun onCaptured(uri: Uri) {
                                    // Navigate back to HomeScreen and pass the Uri as a string, removing CameraScreen from the backstack
                                    val newRoute = HomeDestination(uri.toString())
                                    navController.navigate(newRoute) {
                                        popUpTo(HomeDestination()) { inclusive = true }
                                        restoreState = true
                                        launchSingleTop = true
                                    }
                                }

                                override fun onBackClicked() {
                                    navController.navigate(HomeDestination()) {
                                        popUpTo(HomeDestination()) { inclusive = true }
                                        restoreState = true
                                        launchSingleTop = true
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}