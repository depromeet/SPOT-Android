package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depromeet.presentation.viewfinder.sample.Keyword
import com.depromeet.presentation.viewfinder.sample.keywords

@Composable
fun KeywordCard(
    keyword: Keyword,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFFF4F4F4), shape = RoundedCornerShape(4.dp))
            .padding(
                vertical = 4.dp,
                horizontal = 12.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = keyword.message,
            fontSize = 12.sp,
        )
    }
}

@Preview
@Composable
private fun KeywordCardPreview() {
    KeywordCard(keywords[0])
}