package com.amrk000.repairit.presentation.screen.history

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amrk000.repairit.R
import com.amrk000.repairit.data.model.HistoryItem
import com.amrk000.repairit.data.model.ResultData
import com.amrk000.repairit.presentation.ui.components.shared.ResultView
import com.amrk000.repairit.presentation.ui.theme.GreyDarkMode
import com.amrk000.repairit.presentation.ui.theme.MainTheme
import com.amrk000.repairit.util.ImageEncoder
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asStateFlow

@AndroidEntryPoint
class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainTheme {
                HistoryActivityUi()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryActivityUi() {
    val viewModel: HistoryViewModel = viewModel()
    viewModel.loadHistory()

    val items by viewModel.history.collectAsState()

    var selectedItem by remember { mutableStateOf<HistoryItem?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)){
            Header()

            if(!items.isNullOrEmpty()){
               HistoryList(items!!){ item ->
                   selectedItem = item
               }
            }
            else {
                Box(modifier = Modifier.fillMaxSize()){
                    Text(
                        text = stringResource(id = R.string.history_no_history),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

        }
    }

    ResultView(
        capturedImage = ImageEncoder.decode(selectedItem?.image?:""),
        isSheetOpen = selectedItem != null,
        onDismiss = {
            selectedItem = null
        },
        scanResult = Gson().fromJson(
            selectedItem?.jsonData,
            ResultData::class.java
        )
    )

}

@Composable
fun Header(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)){
        Row(modifier= Modifier
            .align(Alignment.Center)
            .wrapContentWidth()) {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = null,
                modifier = Modifier.size(35.dp))
            Text(
                text = stringResource(id = R.string.history_title),
                modifier = Modifier.padding(start = 5.dp),
                style = TextStyle(fontSize = 28.sp)
            )
        }
    }
}

@Composable
fun HistoryList(items: List<HistoryItem>, onItemClick: (item: HistoryItem) -> Unit){
    LazyColumn(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
    items(items, key = {it.id}){ item ->
            HistoryListItem(item) { onItemClick(item) }
        }
    }
}

@Composable
fun HistoryListItem(item: HistoryItem, onItemClick: (item: HistoryItem) -> Unit){
    val itemResult = Gson().fromJson(
        item.jsonData,
        ResultData::class.java
    )

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
        .requiredHeight(120.dp)
        .clickable(
            enabled = true,
            onClick = { onItemClick(item) },
            indication = rememberRipple(
                bounded = true,
                color = Color.Gray.copy(0.1f),
                radius = 170.dp
            ),
            interactionSource = remember { MutableInteractionSource() }
        ),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(Color.hsv(0f,0.0f,0.5f,0.1f)),
    ) {

        Box(modifier = Modifier.fillMaxSize().padding(10.dp)){

            Row{
                Image(modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .height(120.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(color = Color.Gray.copy(0.1f)),
                    bitmap = ImageEncoder.decode(item.image)?.asImageBitmap()!!,
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(id = R.string.history_image_description)
                )

                Spacer(modifier = Modifier.size(15.dp))

                Column {
                    Spacer(modifier = Modifier.size(10.dp))

                    Text(maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = itemResult.brand + " " + itemResult.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.size(1.dp))

                    Text(maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        text = itemResult.problem,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 12.sp,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.size(5.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = stringResource(id = R.string.history_calendar_icon_description),
                            modifier = Modifier
                                .size(15.dp)
                                .padding(end = 3.dp), tint = GreyDarkMode
                        )
                        Text(
                            text = item.date,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            color = GreyDarkMode
                        )
                    }
                }
            }

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistoryActivityPreview() {
    MainTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Header()
                HistoryList(
                    items = listOf(
                        HistoryItem(
                            id = 1,
                            jsonData = "",
                            image = "",
                            date = ""
                        )
                    )
                ) { }
            }
        }
    }
}