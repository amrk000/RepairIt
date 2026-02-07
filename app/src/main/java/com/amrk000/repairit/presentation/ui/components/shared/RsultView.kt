package com.amrk000.repairit.presentation.ui.components.shared

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

import android.graphics.Bitmap

import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amrk000.repairit.R
import com.amrk000.repairit.data.model.ResultData
import com.amrk000.repairit.presentation.ui.theme.MainColorDarkMode
import com.amrk000.repairit.presentation.ui.theme.MainTheme
import com.amrk000.repairit.presentation.ui.theme.SecondColorDarkMode
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultView(
    isLoading: Boolean = false,
    capturedImage: Bitmap?,
    isSheetOpen: Boolean = false,
    onDismiss: () -> Unit = {},
    scanResult: ResultData? = null,
    error: String? = null
){
    val context = LocalContext.current

    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val takenImageAlphaAnim by animateFloatAsState(
        targetValue = if (capturedImage != null) 1f else 0f,
        animationSpec = tween(500, easing = EaseOutQuad)
    )

    val takenImageScaleAnim by animateFloatAsState(
        targetValue = if (capturedImage != null) 1.0f else 0.85f,
        animationSpec = tween(350, easing = EaseOutQuad)
    )

    if(capturedImage != null) {
        Image(
            bitmap = capturedImage.asImageBitmap(),
            contentDescription = "image",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = takenImageAlphaAnim
                    scaleX = takenImageScaleAnim
                    scaleY = takenImageScaleAnim
                }
                .blur(
                    radiusX = 6.dp,
                    radiusY = 6.dp,
                    edgeTreatment = BlurredEdgeTreatment.Rectangle
                ),
            contentScale = ContentScale.Crop
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .navigationBarsPadding()){

        if(isSheetOpen) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .align(Alignment.TopCenter)
            ){
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if(isLoading) "" else scanResult?.name?: stringResource(id = R.string.result_unknown),
                        fontSize = 40.sp,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            brush = Brush.linearGradient(
                                colors = listOf(MainColorDarkMode, SecondColorDarkMode)
                            ),
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(5f, 5f),
                                blurRadius = 5f
                            )
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    if(isLoading) LinearProgressIndicator(
                        modifier = Modifier
                            .width(80.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(Modifier
                        .wrapContentSize()
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(50.dp)
                        )) {
                        val brand = scanResult?.brand ?: stringResource(id = R.string.result_no_brand)
                        val model = scanResult?.model ?: stringResource(id = R.string.result_no_model)
                        Text(
                            text = if(isLoading) stringResource(id = R.string.result_please_wait) else "$brand | $model",
                            fontSize = 12.sp,
                            fontWeight = FontWeight(100),
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 3.dp, horizontal = 7.dp)
                        )

                    }

                }
            }


            ResultBottomSheet(
                modalSheetState,
                isLoading,
                scanResult,
                error,
                onDismiss = onDismiss,
                onShare = {
                    val share: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, scanResult.toString())
                        type = "text/plain"
                    }

                    context.startActivity(Intent.createChooser(share, context.getString(R.string.common_share_text)))
                },
                onCopy = {
                    val label = context.getString(R.string.result_copy_label)
                    val textData = ClipData.newPlainText(label, scanResult.toString())

                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(textData)
                }
            )
        }
    }

}

@Preview
@Composable
fun ResultViewPreview(){
    MainTheme {
        ResultView(
            isLoading = false,
            capturedImage = null,
            isSheetOpen = true,
            onDismiss = {},
            scanResult = ResultData(
                name = "Mobile",
                brand = "Samsung",
                model = "S25 Ultra",
                mainSpecs = "6GB RAM, 128GB Storage",
                problem = "Screen not turning on",
                tools = listOf("Phillips Screwdriver", "Plastic Spudger"),
                steps = listOf(
                    "Remove back cover",
                    "Disconnect battery",
                    "Replace the screen assembly",
                    "Reassemble and test"
                )
            )
        )
    }
}
