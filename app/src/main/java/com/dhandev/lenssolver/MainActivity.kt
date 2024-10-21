package com.dhandev.lenssolver

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                        startDestination = Destinations.HOME.route
                    ){
                        composable(
                            route = Destinations.HOME.routeWithArgs,
                            arguments = listOf(navArgument(Destinations.HOME.argument) {
                                nullable = true
                                type = NavType.StringType
                            })
                        ) { backStackEntry->
                            val capturedUriString = backStackEntry.arguments?.getString(Destinations.HOME.argument)
                            val capturedUri = capturedUriString?.let { Uri.parse(it) }

                            HomeScreen(
                                modifier = Modifier.safeGesturesPadding(),
                                takenPhotoUri = capturedUri
                            ){
                                navController.navigate(Destinations.CAMERA.route)
                            }
                        }

                        composable(
                            route = Destinations.CAMERA.route
                        ) {
                            CameraScreen(
                                modifier = Modifier.safeGesturesPadding(),
                                delegate = object : CameraDelegate {
                                override fun onCaptured(uri: Uri) {
                                    // Navigate back to HomeScreen and pass the Uri as a string, removing CameraScreen from the backstack
                                    navController.navigate("${Destinations.HOME.route}?${Destinations.HOME.argument}=${Uri.encode(uri.toString())}") {
                                        popUpTo(Destinations.HOME.route) { inclusive = true }
                                    }
                                }

                                override fun onBackClicked() {
                                    navController.navigate(Destinations.HOME.route) {
                                        popUpTo(Destinations.HOME.route) { inclusive = true }
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