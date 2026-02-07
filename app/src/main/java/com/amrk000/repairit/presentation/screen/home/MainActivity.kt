package com.amrk000.repairit.presentation.screen.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.TorchState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amrk000.repairit.R
import com.amrk000.repairit.presentation.ui.components.home.CameraControls
import com.amrk000.repairit.presentation.ui.components.home.CameraView
import com.amrk000.repairit.presentation.ui.components.home.HomeTopBar
import com.amrk000.repairit.presentation.ui.components.shared.ResultView
import com.amrk000.repairit.presentation.ui.components.shared.NetworkState
import com.amrk000.repairit.presentation.ui.components.shared.PermissionHandler
import com.amrk000.repairit.presentation.ui.theme.MainTheme
import com.amrk000.repairit.util.VibratorUtil
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.graphics.createBitmap
import com.amrk000.repairit.presentation.screen.about.AboutActivity
import com.amrk000.repairit.presentation.screen.history.HistoryActivity

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{ false }

        enableEdgeToEdge()
        setContent {
            MainTheme { MainActivityUi() }
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityUi(){
    val context =  LocalContext.current

    val viewModel: MainViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()

    var previewImage by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var isSheetOpen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.capturedImage) {
        previewImage = state.capturedImage
        isSheetOpen = state.capturedImage!=null
    }

    BackHandler {
        viewModel.handleIntent(
            MainIntent.resetState
        )
    }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val image = result.data?.data
            previewImage = MediaStore.Images.Media.getBitmap(context.contentResolver, image)

            viewModel.handleIntent(
                MainIntent.analyzeImage(previewImage!!)
            )
        }
    }

    val permissionDeniedMessage = stringResource(id = R.string.main_camera_permission_denied)
    val cameraPermission = PermissionHandler(
        Manifest.permission.CAMERA,
        onDenied = {
            Toast.makeText(context, permissionDeniedMessage, Toast.LENGTH_SHORT).show()
        }
    )

    var isCameraPermissionAllowed by remember {
        mutableStateOf(ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    if(!isCameraPermissionAllowed) {
        LaunchedEffect(key1 = Unit) {
            cameraPermission.invoke {
                isCameraPermissionAllowed = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    val isNetworkAvailable = NetworkState()

    val camera = CameraView(viewModel.imageCapture)

    var flashOn by remember { mutableStateOf(false) }

    LaunchedEffect(state.isLoading) {
        if(state.isLoading){
            flashOn = false
            camera.cameraControl.enableTorch(flashOn)
        }
    }

    Box(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {

        HomeTopBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding(),
            onInfoClick = {
                context.startActivity(Intent(context, AboutActivity::class.java))
            },
            onHistoryClick = {
                context.startActivity(Intent(context, HistoryActivity::class.java))
            }
        )

        CameraControls(
            isCameraEnabled = isCameraPermissionAllowed,
            isNetworkAvailable = isNetworkAvailable,
            captureImage = {
                cameraPermission.invoke {
                    isCameraPermissionAllowed = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

                    VibratorUtil.vibrate(
                        context,
                        50
                    )

                    viewModel.handleIntent(
                        MainIntent.captureImageAndAnalyze
                    )
                }
            },
            loadImage = {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                imagePicker.launch(intent)
            },
            isFlashOn = flashOn,
            toggleFlash = {
                camera.cameraControl.enableTorch(camera.cameraInfo.torchState.value != TorchState.ON)
                flashOn = !flashOn
            }
        )

        ResultView(
            isLoading = state.isLoading,
            capturedImage = previewImage,
            isSheetOpen = isSheetOpen,
            onDismiss = {
                isSheetOpen = false
                previewImage = null
                viewModel.handleIntent(
                    MainIntent.resetState
                )
            },
            scanResult = state.resultData,
            error = state.error
        )
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun Preview() {
    val bitmap = createBitmap(1000, 1000)
    Canvas(bitmap).drawColor(Color.WHITE)

    MainTheme {
        Box(modifier = Modifier.fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.Black)
            .navigationBarsPadding()
        ) {
            HomeTopBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding(),
                onInfoClick = {

                },
                onHistoryClick = {

                }
            )

            CameraControls(
                isCameraEnabled = true,
                isNetworkAvailable = true,
                captureImage = { },
                loadImage = { },
            ) { }
//        ResultView(
//            isLoading = false,
//            capturedImage = bitmap,
//            isSheetOpen = true,
//            onDismiss = {
//
//            },
//            scanResult = null
//        )
        }
    }
}
