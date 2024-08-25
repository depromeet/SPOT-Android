package com.dpm.presentation.util

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.dpm.designsystem.compose.ui.SpotTheme

@Composable
fun MultiStyleText(modifier: Modifier = Modifier, style: TextStyle, vararg textWithColors: Pair<String, Color>) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            textWithColors.forEach { (text, color) ->
                withStyle(style = SpanStyle(color = color)) {
                    append(text)
                }
            }
        },
        style = style
    )
}

@Composable
fun HighlightedText(
    text: String,
    style: TextStyle,
    highlightRanges: List<IntRange>,
    modifier: Modifier = Modifier
) {
    val annotatedString = buildAnnotatedString {
        var lastIndex = 0

        for (range in highlightRanges) {
            append(text.substring(lastIndex, range.first))
            withStyle(style = SpanStyle(color = SpotTheme.colors.actionEnabled)) {
                append(text.substring(range.first, range.last + 1))
            }
            lastIndex = range.last + 1
        }

        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }

    Text(
        text = annotatedString,
        style = style,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}
