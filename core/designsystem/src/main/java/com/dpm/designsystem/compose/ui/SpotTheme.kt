package com.dpm.designsystem.compose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSpotTypography = staticCompositionLocalOf {
    SpotTypography()
}

val LocalSpotColors = staticCompositionLocalOf {
    SpotColor()
}

@Composable
fun SpotTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    colors: SpotColor = SpotColor(),
    typography: SpotTypography = SpotTypography(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalSpotTypography provides typography,
        LocalSpotColors provides colors,
        content = content
    )
}

object SpotTheme {
    val colors: SpotColor
        @Composable
        @ReadOnlyComposable
        get() = LocalSpotColors.current

    val typography: SpotTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalSpotTypography.current
}