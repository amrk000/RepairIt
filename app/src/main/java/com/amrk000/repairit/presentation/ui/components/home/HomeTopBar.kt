package com.amrk000.repairit.presentation.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amrk000.repairit.R

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    onInfoClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo_dark),
            contentDescription = stringResource(id = R.string.home_logo_description),
            modifier = Modifier
                .wrapContentWidth()
                .height(110.dp)
                .align(Alignment.Center)
                .padding(top = 5.dp)
        )

        IconButton(
            onClick = onInfoClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(id = R.string.home_about_description),
                modifier = Modifier.size(35.dp),
                tint = Color.White
            )
        }

        IconButton(
            onClick = onHistoryClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = stringResource(id = R.string.home_history_description),
                modifier = Modifier.size(35.dp),
                tint = Color.White
            )
        }
    }
}