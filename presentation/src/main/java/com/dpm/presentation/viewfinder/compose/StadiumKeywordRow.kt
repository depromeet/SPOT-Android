package com.dpm.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Composable
fun StadiumKeywordRow(
    keyword: Keyword,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = if (keyword.type == 0) {
                    SpotTheme.colors.backgroundPositive
                } else {
                    SpotTheme.colors.backgroundNegative
                },
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                vertical = 10.dp, horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = keyword.message,
            style = SpotTheme.typography.label08,
            color = SpotTheme.colors.foregroundHeading
        )
        Text(
            text = keyword.like.toString(),
            style = SpotTheme.typography.label03,
            color = if (keyword.type == 0) {
                SpotTheme.colors.actionEnabled
            } else {
                SpotTheme.colors.errorPrimary
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