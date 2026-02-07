package com.amrk000.repairit.presentation.ui.components.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Details
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amrk000.repairit.R
import com.amrk000.repairit.data.model.ResultData
import com.amrk000.repairit.presentation.ui.theme.ErrorColor
import com.amrk000.repairit.presentation.ui.theme.ErrorColorSecondary
import com.amrk000.repairit.presentation.ui.theme.MainColorDarkMode
import com.amrk000.repairit.presentation.ui.theme.MainTheme
import com.amrk000.repairit.presentation.ui.theme.SecondColorDarkMode
import kotlin.collections.listOf

private data class SectionData(
    val icon: ImageVector,
    val title: String,
    val text: String? = null,
    val list: List<String>? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultBottomSheet(
    modalSheetState: SheetState,
    isLoading: Boolean = false,
    scanResult: ResultData?,
    error: String? = null,
    onDismiss: () -> Unit,
    onShare: () -> Unit,
    onCopy: () -> Unit
){
    val specsTitle = stringResource(id = R.string.result_section_specs)
    val problemTitle = stringResource(id = R.string.result_section_problem)
    val toolsTitle = stringResource(id = R.string.result_section_tools)
    val stepsTitle = stringResource(id = R.string.result_section_steps)
    val doneTitle = stringResource(id = R.string.result_section_done)

    var sections by remember { mutableStateOf(listOf<SectionData>()) }

    val animationProgress = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(scanResult, specsTitle, problemTitle, toolsTitle, stepsTitle, doneTitle) {
        if(!isLoading && scanResult != null){

            sections = listOf(
                SectionData(icon= Icons.Outlined.Info, title= specsTitle, text= scanResult.mainSpecs),
                SectionData(icon= Icons.Outlined.ReportProblem, title= problemTitle, text= scanResult.problem),
                SectionData(icon= Icons.Rounded.Work, title= toolsTitle, list= scanResult.tools),
                SectionData(icon= Icons.Rounded.Build, title= stepsTitle, list= scanResult.steps),
                SectionData(icon= Icons.Outlined.Check, title= doneTitle)
            )

            animationProgress.animateTo(
                targetValue = sections.size.toFloat(),
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = EaseOutQuad
                )
            )

        }
    }

    val localDensity = LocalDensity.current
    var columnHeightDp by remember { mutableStateOf(0.dp) }

        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(),
            windowInsets = WindowInsets(0,0,0, 0),
            dragHandle = null,
            scrimColor = Color.Transparent,
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            sheetState = modalSheetState,
            onDismissRequest = onDismiss
        ) {

            Box(modifier = Modifier.fillMaxSize()){

                if(isLoading){
                    Box(modifier = Modifier.fillMaxSize()){
                        Column(
                            modifier= Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .wrapContentSize()
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                MainColorDarkMode,
                                                SecondColorDarkMode
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .padding(10.dp)
                                        .align(Alignment.Center),
                                    color = Color.White
                                )

                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(5.dp)
                                        .align(Alignment.Center),
                                    tint = Color.White,
                                    contentDescription = null
                                )
                            }

                            Spacer(modifier = Modifier.size(15.dp))

                            Text(
                                text = stringResource(id = R.string.result_diagnosing),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    brush = Brush.linearGradient(
                                        colors = listOf(SecondColorDarkMode, MainColorDarkMode)
                                    )
                                ),
                            )
                        }
                    }
                }
                else {

                    if(scanResult == null || error != null){
                        Box(modifier = Modifier.fillMaxSize()){
                            Column(
                                modifier= Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .wrapContentSize()
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    ErrorColor,
                                                    ErrorColorSecondary
                                                )
                                            ),
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.ErrorOutline,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .padding(15.dp)
                                            .align(Alignment.Center),
                                        tint = Color.White,
                                        contentDescription = stringResource(id = R.string.result_error_icon_description)
                                    )
                                }

                                Spacer(modifier = Modifier.size(15.dp))

                                Text(
                                    text = stringResource(id = R.string.result_error_title),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(
                                        brush = Brush.linearGradient(
                                            colors = listOf(ErrorColor, ErrorColorSecondary)
                                        )
                                    ),
                                )

                                Text(
                                    text = error ?: stringResource(id = R.string.result_unknown_error),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Light
                                )

                            }
                        }
                    }
                    else if(scanResult.problem.isEmpty() || scanResult.steps.isEmpty()){
                        Box(modifier = Modifier.fillMaxSize()){
                            Column(
                                modifier= Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .wrapContentSize()
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    MainColorDarkMode,
                                                    SecondColorDarkMode
                                                )
                                            ),
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .padding(15.dp)
                                            .align(Alignment.Center),
                                        tint = Color.White,
                                        contentDescription = stringResource(id = R.string.result_check_icon_description)
                                    )
                                }

                                Spacer(modifier = Modifier.size(15.dp))

                                Text(
                                    text = stringResource(id = R.string.result_no_problem),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(
                                        brush = Brush.linearGradient(
                                            colors = listOf(SecondColorDarkMode, MainColorDarkMode)
                                        )
                                    ),
                                )
                            }
                        }
                    }
                    else {
                        Column(
                            Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(bottom = 60.dp, start = 20.dp, end = 20.dp)
                        ) {
                            Spacer(modifier = Modifier.height(70.dp))

                            Box(modifier = Modifier.fillMaxSize()) {
                                VerticalProgressIndicator(
                                    progress = (animationProgress.value / sections.size).coerceIn(
                                        0f,
                                        1f
                                    ),
                                    modifier = Modifier.align(Alignment.TopStart)
                                        .height(columnHeightDp)
                                        .width(20.dp)
                                        .padding(start = 15.dp, top = 15.dp, bottom = 40.dp)
                                )

                                SelectionContainer(modifier = Modifier.align(Alignment.TopStart)) {
                                    Column(
                                        modifier = Modifier.onGloballyPositioned { coordinates ->
                                            val heightInPx = coordinates.size.height
                                            columnHeightDp =
                                                with(localDensity) { heightInPx.toDp() }
                                        }
                                    ) {

                                        sections.forEachIndexed { index, item ->
                                            val isVisible = animationProgress.value > index

                                            AnimatedVisibility(
                                                visible = isVisible,
                                                enter = fadeIn(
                                                    animationSpec = tween(
                                                        durationMillis = 500,
                                                        delayMillis = 100 * index,
                                                        easing = EaseInOutQuad
                                                    )
                                                ) + slideInVertically(
                                                    initialOffsetY = { 30 },
                                                    animationSpec = tween(
                                                        durationMillis = 500,
                                                        delayMillis = 100 * index,
                                                        easing = EaseInOutQuad
                                                    )
                                                )
                                            ) {
                                                ResultSection(
                                                    icon = item.icon,
                                                    title = item.title
                                                ) {
                                                    item.text?.let {
                                                        Text(item.text)
                                                    }

                                                    item.list?.let {
                                                        val isRepairSteps = index == 3

                                                        Column {
                                                            it.forEachIndexed { i, it ->
                                                                val itemStart =
                                                                    if (isRepairSteps) "${i + 1}- " else "• "
                                                                Text(
                                                                    text = itemStart + it + "\n",
                                                                    lineHeight = 15.sp
                                                                )
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                ResultBottomSheetTop(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        )
                        .align(Alignment.TopCenter),
                    onShare = onShare,
                    onCopy = onCopy
                )

            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultBottomSheetTop(modifier: Modifier = Modifier, onShare: () -> Unit, onCopy: () -> Unit){
    Box(
        modifier = modifier
    ){

        BottomSheetDefaults.DragHandle(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 0.dp)
                .wrapContentWidth()
                .align(Alignment.TopCenter)
        )

        FilledIconButton(
            onClick = onShare,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 15.dp, vertical = 5.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color(0, 0, 0, 210),
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = stringResource(id = R.string.common_share)
            )
        }

        FilledIconButton(
            onClick = onCopy,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 15.dp, vertical = 5.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color(0, 0, 0, 210),
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.ContentCopy,
                contentDescription = stringResource(id = R.string.common_copy)
            )
        }
    }
}

@Composable
fun VerticalProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = Color.Transparent,
    strokeCap: StrokeCap = StrokeCap.Round
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.width
        val height = size.height

        drawLine(
            color = trackColor,
            start = Offset(x = strokeWidth / 2, y = 0f),
            end = Offset(x = strokeWidth / 2, y = height),
            strokeWidth = strokeWidth,
            cap = strokeCap
        )

        drawLine(
            color = color,
            start = Offset(x = strokeWidth / 2, y = 0f),
            end = Offset(x = strokeWidth / 2, y = height * progress),
            strokeWidth = strokeWidth,
            cap = strokeCap
        )
    }
}

@Composable
fun ResultSection(icon: ImageVector, title: String, content: @Composable () -> Unit = {}){
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .padding(1.dp)
                    .wrapContentSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MainColorDarkMode,
                                SecondColorDarkMode
                            )
                        ),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = icon,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(5.dp)
                        .align(Alignment.Center),
                    tint = Color.White,
                    contentDescription = stringResource(id = R.string.result_label_icon_description)
                )
            }

            Spacer(modifier = Modifier.size(5.dp))

            Text(
                text = title,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(SecondColorDarkMode, MainColorDarkMode)
                    )
                ),
            )

        }

        Spacer(modifier = Modifier.size(5.dp))

        Box(modifier = Modifier.padding(start = 40.dp).wrapContentSize()) {
            content()
        }

        Spacer(modifier = Modifier.size(20.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ResultBottomSheetPreview(){
    MainTheme {
        val resultMock = ResultData(
            name = "Item Name",
            brand = "Brand",
            model = "Model",
            mainSpecs = "Main Specs",
            problem = "Problem",
            tools = listOf("Tool 1", "Tool 2", "Tool 3", "Tool 4"),
            steps = listOf("Step 1", "Step 2", "Step 3", "Step 4")
        )

        ResultBottomSheet(
            modalSheetState = rememberModalBottomSheetState(true, {true}),
            isLoading = false,
            scanResult = resultMock,
            onDismiss = {},
            onShare = {},
            onCopy = {}
        )
    }
}