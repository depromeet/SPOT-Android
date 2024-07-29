package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.presentation.viewfinder.sample.Keyword

@Composable
fun StadiumKeywordRow(
    keyword: Keyword,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (keyword.type == 0) {
                    Color(0xFFECFBF5)
                } else {
                    Color(0xFFFFF1EF)
                },
                shape = RoundedCornerShape(6.dp)
            )
            .padding(
                vertical = 10.dp, horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = keyword.message,
            fontSize = 14.sp,
            color = Color(0xFF222222)
        )
        Text(
            text = keyword.like.toString(),
            fontSize = 14.sp,
            color = if (keyword.type == 0) {
                Color(0xFF43D596)
            } else {
                Color(0xFFFD7563)
            }
        )
    }
}

@Preview
@Composable
private fun StadiumKeywordRowPreview() {
    StadiumKeywordRow(
        keyword = Keyword("🙍‍서서 응원하는 존", 44, 1)
    )
}

@Preview
@Composable
private fun StadiumKeywordRowPositivePreview() {
    StadiumKeywordRow(
        keyword = Keyword("🙍‍서서 응원하는 존", 44, 0)
    )
}