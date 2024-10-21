package com.dhandev.lenssolver

import android.os.Bundle
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
import com.dhandev.lenssolver.ui.theme.LensSolverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LensSolverTheme {
                val safeGestureInset = WindowInsets.safeGestures.asPaddingValues()
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = safeGestureInset.calculateBottomPadding()),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    HomeScreen(
                        modifier = Modifier.safeGesturesPadding()
                    ){}
                }
            }
        }
    }
}