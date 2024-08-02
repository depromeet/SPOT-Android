package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.presentation.R
import com.depromeet.presentation.extension.noRippleClickable
import com.depromeet.presentation.viewfinder.sample.Keyword

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KeywordFlowRow(
    keywords: List<Keyword>,
    overflowIndex: Int = 4,
    isSelfExpanded: Boolean = true,
    modifier: Modifier = Modifier,
    onActionCallback: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    val displayKeywords = if (expanded) {
        keywords
    } else {
        val maxLines = 10
        val overflowIndex = overflowIndex
        val displayedCount = if (overflowIndex != -1) overflowIndex else keywords.size
        keywords.take(displayedCount.coerceAtMost(maxLines * 3)) // 3개씩 2줄까지만 보여줌
    }

    FlowRow(
        modifier = modifier,
    ) {
        displayKeywords.forEach { keyword ->
            KeywordCard(keyword = keyword, modifier = Modifier.padding(bottom = 6.dp, end = 6.dp))
        }

        if (!expanded && displayKeywords.size < keywords.size) {
            val overflowCount = keywords.size - displayKeywords.size
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .background(
                        color = SpotTheme.colors.backgroundSecondary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .noRippleClickable {
                        if (isSelfExpanded) {
                            expanded = true
                        } else {
                            onActionCallback()
                        }
                    }
                    .padding(
                        horizontal = 7.dp,
                        vertical = 6.dp
                    ),
            ) {
                Text(
                    text = stringResource(id = R.string.viewfinder_plus_count, overflowCount),
                    style = SpotTheme.typography.label10,
                    color = SpotTheme.colors.foregroundBodySebtext
                )
            }
        }
    }
}

@Preview
@Composable
private fun KeywordFlowOneLinePreview() {
    KeywordFlowRow(
        keywords = listOf(
            Keyword("🙍‍서서 응원하는 존", 44, 0),
            Keyword("🙍‍서서 응원하는 존", 44, 0)
        )
    )
}

@Preview
@Composable
private fun KeywordFlowRowTwoLinePreview() {
    KeywordFlowRow(
        keywords = listOf(
            Keyword("🙍‍서서 응원하는 존", 44, 0),
            Keyword("☀️ 온종일 햇빛 존", 44, 1),
            Keyword("🙍‍서서 응원하는 존", 44, 1),
            Keyword("🙍‍서서 응원하는 존", 44, 0),
            Keyword("🙍‍서서 응원하는 존", 44, 0)
        )
    )
}

@Preview
@Composable
private fun KeywordFlowRowTwoLineOverflowPreview() {
    KeywordFlowRow(
        keywords = listOf(
            Keyword("🙍‍서서 응원하는 존", 44, 0),
            Keyword("☀️ 온종일 햇빛 존", 44, 1),
            Keyword("🙍‍서서 응원하는 존", 44, 1),
            Keyword("🙍‍서서 응원하는 존", 44, 0),
            Keyword("🙍‍서서 응원하는 존", 44, 0),
            Keyword("🙍‍서서 응원하는 존", 44, 0),
            Keyword("🙍‍서서 응원하는 존", 44, 0)
        )
    )
}