package com.dpm.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.presentation.viewfinder.sample.Keyword
import com.dpm.presentation.viewfinder.sample.keywords

@Composable
fun KeywordCard(
    keyword: Keyword,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = SpotTheme.colors.backgroundSecondary,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = keyword.message,
            style = SpotTheme.typography.label10,
            color = SpotTheme.colors.foregroundBodySebtext
        )
    }
}

@Preview
@Composable
private fun KeywordCardPreview() {
    KeywordCard(keywords[0])
}