package com.dhandev.lenssolver

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dhandev.lenssolver.ui.theme.LensSolverTheme
import com.dhandev.lenssolver.ui.theme.Pink40
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(),
    takenPhotoUri: Uri? = null,
    doTakePicture: ()->Unit
) {
    val placeholderResult = stringResource(R.string.results_placeholder)
    var pickedImage by remember { mutableStateOf<Uri?>(null) }
    var prompt by rememberSaveable { mutableStateOf(Utils.PROMPT) }
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showInfoDialog by rememberSaveable { mutableStateOf(false) }
    val uiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        it?.let { pickedImage = it }
    }
    LaunchedEffect(takenPhotoUri) { if (takenPhotoUri != null) pickedImage = takenPhotoUri }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            doTakePicture()
        } else {
            Toast.makeText(
                context,
                "Camera Permission should be Granted to Take Image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    val scope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            confirmValueChange = {
                when (it) {
                    SheetValue.PartiallyExpanded -> isExpanded= false
                    SheetValue.Expanded -> isExpanded= true
                    else -> {/**do nothing*/}
                }
                true
            }
        )
    )
    val peekSheetHeight = 128.dp

    Box{
        BottomSheetScaffold(
            modifier = modifier,
            scaffoldState = scaffoldState,
            sheetPeekHeight = peekSheetHeight,
            sheetContent = {
                Column(
                    Modifier.fillMaxSize(0.95f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(visible = isExpanded.not()) {
                        Row(modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(peekSheetHeight))
                        {
                            Button(
                                onClick = {
                                    pickedImage = null
                                    result = placeholderResult
                                    prompt = Utils.PROMPT
                                },
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = Pink40
                                )
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.baseline_restart_alt_24),
                                    contentDescription = null
                                )
                            }
                            Button(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .fillMaxWidth(),
                                enabled = prompt.isNotEmpty() && pickedImage != null,
                                onClick = {
                                    val bitmap: Bitmap? = Utils.decodeUriToBitmap(context, pickedImage)
                                    bitmap?.let {
                                        homeViewModel.sendPrompt(it, prompt)
                                    }
                                    scope.launch {
                                        scaffoldState.bottomSheetState.expand()
                                    }
                                }
                            ) {
                                Text(text = stringResource(id = R.string.solve))
                            }
                        }
                    }
                    // Expanded Content

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ){
                        if (uiState is UiState.Loading) {
                            CircularProgressIndicator()
                        } else {
                            var textColor = MaterialTheme.colorScheme.onSurface
                            if (uiState is UiState.Error) {
                                textColor = MaterialTheme.colorScheme.error
                                result = (uiState as UiState.Error).errorMessage
                            } else if (uiState is UiState.Success) {
                                textColor = MaterialTheme.colorScheme.onSurface
                                result = (uiState as UiState.Success).outputText
                            }
                            val scrollState = rememberScrollState()
                            Log.d("RESULT","Ini raw text = $result")
                            if (pickedImage == null){
                                Text(
                                    text = placeholderResult,
                                    textAlign = TextAlign.Center,
                                    color = textColor
                                )
                            } else {
                                Text(
                                    text = Utils.formatText(result),
                                    textAlign = TextAlign.Start,
                                    color = textColor,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(scrollState)
                                )
                            }
                        }
                    }
                }
            }
        ) {
            val baseScrolllState = rememberScrollState()

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(bottom = 70.dp)
                    .verticalScroll(baseScrolllState)
            ){

                Box(Modifier.padding(bottom = 16.dp)) {
                    Text(
                        text = "Lens Solver",
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Start,
                        color = Pink40,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Image(
                        painter = painterResource(id = R.drawable.baseline_info_outline_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Pink40),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable {
                                showInfoDialog = true
                            }
                    )
                }

                val imageModifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(
                        indication = ripple(bounded = true),
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        showDialog = true
                    }
                AnimatedContent(targetState = pickedImage, label = "Image") {
                    if (it == null){
                        Surface(
                            modifier = imageModifier,
                            color = Color.Transparent
                        ) {
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.baseline_add_photo_alternate_24),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(150.dp),
                                    colorFilter = ColorFilter.tint(Pink40)
                                )
                                Text(
                                    text = "Add Math or Physics Problem",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Pink40
                                )
                            }
                        }
                    } else {
                        AsyncImage(
                            modifier = imageModifier,
                            model = pickedImage,
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }

                }

                PromptInputComp(
                    Modifier.padding(top = 8.dp),
                    value = if (prompt == Utils.PROMPT) "" else prompt
                ) {newPrompt->
                    prompt = newPrompt
                }
            }
        }
        AnimatedVisibility(
            visible = showDialog,
            exit = ExitTransition.None
        ) {
            LensDialog(
                onDismiss = { showDialog = false },
                onCamera = {
                    if (cameraPermissionState.status.isGranted) {
                        doTakePicture()
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                onPicker = {
                    imagePicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
        }
        AnimatedVisibility(
            visible = showInfoDialog,
            exit = ExitTransition.None
        ) {
            InfoDialog { showInfoDialog = false }
        }

    }

    BackHandler(
        enabled = isExpanded
    ) {
        scope.launch {
            scaffoldState.bottomSheetState.partialExpand()
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    LensSolverTheme {
        HomeScreen(takenPhotoUri = null){}
    }
}