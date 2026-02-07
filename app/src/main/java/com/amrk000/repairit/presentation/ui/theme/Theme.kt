package com.amrk000.repairit.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MainColorDarkMode,
    secondary = GreyDarkMode,
    tertiary = GreyDarkMode,
    onPrimary = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = MainColorLightMode,
    secondary = GreyLightMode,
    tertiary = GreyLightMode,
    onPrimary = Color.White

)

@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}