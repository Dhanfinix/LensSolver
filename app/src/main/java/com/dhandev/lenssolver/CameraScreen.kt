package com.dhandev.lenssolver

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.WindowManager
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dhandev.lenssolver.component.CameraGridComp
import java.io.File
import kotlin.random.Random

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    delegate: CameraDelegate
) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(localContext)
    }
    val recomposeFlag by rememberSaveable { mutableIntStateOf(Random.nextInt()) }

    // Initialize imageCapture
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var flashActive by rememberSaveable { mutableStateOf(false) }
    var showGrid by rememberSaveable { mutableStateOf(false) }

    Box {
        key(recomposeFlag) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    val previewView = PreviewView(context)

                    // Initialize Preview and ImageCapture
                    val preview = Preview.Builder().build()
                    imageCapture = ImageCapture.Builder().build()

                    val selector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    val orientationEventListener = object : OrientationEventListener(localContext) {
                        override fun onOrientationChanged(orientation: Int) {
                            val rotation: Int = when (orientation) {
                                in 45..134 -> Surface.ROTATION_270
                                in 135..224 -> Surface.ROTATION_180
                                in 225..314 -> Surface.ROTATION_90
                                else -> Surface.ROTATION_0
                            }
                            imageCapture?.targetRotation = rotation
                        }
                    }
                    orientationEventListener.enable()

                    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    val rotation = windowManager.defaultDisplay?.rotation ?: Surface.ROTATION_0
                    imageCapture?.targetRotation = rotation

                    runCatching {
                        val cameraProvider = cameraProviderFuture.get()

                        // Bind the Preview and ImageCapture use cases
                        val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            imageCapture // Ensure imageCapture is bound here
                        )
                        cameraControl = camera.cameraControl
                    }.onFailure {
                        Log.e("CAMERA", "Camera bind error ${it.localizedMessage}", it)
                    }

                    previewView
                }
            )
        }

        Row(
            modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(end = 13.dp)
                    .clickable { delegate.onBackClicked() },
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null,
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.take_problem_image),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Image(
                modifier = Modifier
                    .padding(end = 24.dp)
                    .clickable { showGrid = !showGrid },
                painter = painterResource(id = R.drawable.baseline_grid_3x3_24),
                contentDescription = null
            )
            Image(
                modifier = Modifier
                    .clickable {
                        flashActive = !flashActive
                        cameraControl?.enableTorch(flashActive)
                    },
                painter = painterResource(id = R.drawable.baseline_flash_on_24),
                contentDescription = null
            )
        }

        Button(
            onClick = {
                captureImage(localContext, imageCapture) {
                    delegate.onCaptured(it)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = "Take Picture")
        }

        if (showGrid) {
            CameraGridComp()
        }
    }
}

private fun captureImage(context: Context, imageCapture: ImageCapture?, onImageTaken: (Uri) -> Unit) {
    val photoFile = File(
        context.externalMediaDirs.firstOrNull(),
        "IMG_${System.currentTimeMillis()}.jpg"
    )
    val photoUri = Uri.fromFile(photoFile)

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture?.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onImageTaken(photoUri)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("ERROR", "Image capture failed: ${exception.localizedMessage}")
            }
        }
    )
}

