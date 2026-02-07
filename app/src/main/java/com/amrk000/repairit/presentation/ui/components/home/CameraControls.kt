package com.amrk000.repairit.presentation.ui.components.home

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.FlashlightOff
import androidx.compose.material.icons.outlined.FlashlightOn
import androidx.compose.material.icons.outlined.LibraryAdd
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.ConfigurationCompat
import com.amrk000.repairit.R
import com.amrk000.repairit.presentation.ui.theme.MainColorDarkMode

@Composable
fun CameraControls(
    isCameraEnabled: Boolean = true,
    isNetworkAvailable: Boolean = true,
    isFlashOn: Boolean = false,
    captureImage: () -> Unit,
    loadImage: () -> Unit,
    toggleFlash: () -> Unit
) {
    val context = LocalContext.current

    val configuration = LocalConfiguration.current

    val currentLocale = ConfigurationCompat.getLocales(configuration)[0]

    val language = currentLocale?.displayLanguage

    val animatedContainerColor by animateColorAsState(
        targetValue = if (!isNetworkAvailable) Color.Red else MainColorDarkMode,
        animationSpec = tween(durationMillis = 700),
        label = "ButtonColorAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(bottom = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .wrapContentSize()
        ) {
            OutlinedButton(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS)
                        intent.data = Uri.fromParts("package", context.packageName, null)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.following_system_language),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(modifier = Modifier
                        .size(15.dp)
                        .padding(end = 3.dp),
                        imageVector = Icons.Default.Translate,
                        tint = Color.White,
                        contentDescription = null
                    )
                    Text(
                        text= language?:"Default",
                        fontSize = 10.sp,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.size(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .wrapContentHeight()
                    .background(
                        color = Color.Black.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .padding(3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {

                FloatingActionButton(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(5.dp)
                        .alpha(if (isNetworkAvailable) 1f else 0.5f),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(4.dp),
                    containerColor = Color.Black,
                    contentColor = MainColorDarkMode,
                    onClick = { if (isNetworkAvailable) loadImage() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LibraryAdd,
                        contentDescription = stringResource(id = R.string.home_load_image_description),
                        modifier = Modifier.size(20.dp)
                    )
                }

                FloatingActionButton(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(5.dp)
                        .graphicsLayer(
                            scaleX = 1.75f,
                            scaleY = 1.75f
                        ),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(4.dp),
                    containerColor = animatedContainerColor,
                    contentColor = Color.White,
                    onClick = { if (isNetworkAvailable) captureImage() }
                ) {
                    AnimatedContent(
                        targetState = isNetworkAvailable,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                                    scaleIn(
                                        initialScale = 0.92f,
                                        animationSpec = tween(220, delayMillis = 90)
                                    ) togetherWith
                                    fadeOut(animationSpec = tween(90))
                        },
                        label = "IconAnimation"
                    ) { networkAvailable ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            Icon(
                                imageVector = if (!networkAvailable) {
                                    Icons.Outlined.WifiOff
                                } else {
                                    Icons.Rounded.Build
                                },
                                contentDescription = stringResource(id = R.string.home_capture_description),
                                modifier = Modifier
                                    .size(18.dp)
                                    .align(Alignment.Center)
                            )

                            if (!networkAvailable || !isCameraEnabled) {
                                Text(
                                    text = if (!networkAvailable) stringResource(id = R.string.home_disconnected) else stringResource(
                                        id = R.string.home_allow_cam
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 7.dp),
                                    color = Color.White,
                                    fontSize = 5.sp,
                                    lineHeight = 6.sp
                                )
                            }
                        }
                    }
                }

                FloatingActionButton(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(5.dp),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(4.dp),
                    containerColor = Color.Black,
                    contentColor = MainColorDarkMode,
                    onClick = { toggleFlash() }
                ) {
                    Icon(
                        imageVector = if (isFlashOn) Icons.Outlined.FlashlightOn else Icons.Outlined.FlashlightOff,
                        contentDescription = stringResource(id = R.string.home_toggle_flash_description),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}