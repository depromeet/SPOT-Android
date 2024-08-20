package com.dpm.presentation.util

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun MultiStyleText(style: TextStyle, vararg textWithColors: Pair<String, Color>) {
    Text(
        buildAnnotatedString {
            textWithColors.forEach { (text, color) ->
                withStyle(style = SpanStyle(color = color)) {
                    append(text)
                }
            }
        },
        style = style
    )
}