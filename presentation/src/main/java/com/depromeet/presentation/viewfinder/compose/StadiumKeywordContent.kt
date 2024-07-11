package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.depromeet.presentation.R
import com.depromeet.presentation.viewfinder.sample.Keyword

@Composable
fun StadiumKeywordContent(
    isMore: Boolean,
    keywords: List<Keyword>,
    modifier: Modifier = Modifier,
    onChangeIsMore: (isMore: Boolean) -> Unit,
) {
    Column {
        Spacer(modifier = Modifier.height(20.dp))
        if (isMore) {
            repeat(keywords.size) {
                StadiumKeywordRow(
                    keyword = keywords[it],
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "가장 많이 선정된 ${keywords.size}개의 키워드만 보여줘요",
                fontSize = 12.sp,
                color = Color(0xFF606060),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_up),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { onChangeIsMore(false) }
            )
        } else {
            when {
                keywords.isEmpty() -> Unit

                keywords.size in 1..3 -> {
                    repeat(keywords.size) {
                        StadiumKeywordRow(
                            keyword = keywords[it],
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "가장 많이 선정된 ${keywords.size}개의 키워드만 보여줘요",
                        fontSize = 12.sp,
                        color = Color(0xFF606060),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                keywords.size > 3 -> {
                    repeat(3) {
                        if (it == 2) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                DownKeywordBlind(
                                    modifier = Modifier.zIndex(1f),
                                    onClickMore = { onChangeIsMore(true) }
                                )
                                StadiumKeywordRow(
                                    keyword = keywords[it],
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        } else {
                            StadiumKeywordRow(
                                keyword = keywords[it],
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DownKeywordBlind(
    modifier: Modifier = Modifier,
    onClickMore: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(listOf(Color.Transparent, Color.White)),
                alpha = 1f
            )
            .clickable { onClickMore() }, contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_down),
            contentDescription = null,
            modifier = Modifier.padding(
                vertical = 25.dp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumKeywordContentOnePreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StadiumKeywordContent(
            isMore = false,
            keywords = listOf(Keyword("🙍‍서서 응원하는 존", 44, 0))
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumKeywordContentTwoPreview() {
    Column {
        StadiumKeywordContent(
            isMore = false,
            keywords = listOf(
                Keyword("🙍‍서서 응원하는 존", 44, 0),
                Keyword("☀️ 온종일 햇빛 존", 44, 1),
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumKeywordContentThreePreview() {
    Column {
        StadiumKeywordContent(
            isMore = false,
            keywords = listOf(
                Keyword("🙍‍서서 응원하는 존", 44, 0),
                Keyword("☀️ 온종일 햇빛 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 0),
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumKeywordContentFourPreview() {
    Column {
        StadiumKeywordContent(
            isMore = false,
            keywords = listOf(
                Keyword("🙍‍서서 응원하는 존", 44, 0),
                Keyword("☀️ 온종일 햇빛 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 0),
                Keyword("☀️ 온종일 햇빛 존", 44, 1),
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumKeywordContentFivePreview() {
    Column {
        StadiumKeywordContent(
            isMore = false,
            keywords = listOf(
                Keyword("🙍‍서서 응원하는 존", 44, 0),
                Keyword("☀️ 온종일 햇빛 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 0),
                Keyword("☀️ 온종일 햇빛 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 0),
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumKeywordContentFiveMorePreview() {
    Column(
        modifier = Modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StadiumKeywordContent(
            isMore = true,
            keywords = listOf(
                Keyword("🙍‍서서 응원하는 존", 44, 0),
                Keyword("☀️ 온종일 햇빛 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 0),
                Keyword("☀️ 온종일 햇빛 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 0),
            )
        ) {}
    }
}

@Preview
@Composable
private fun DownKeywordBlindPreview() {
    DownKeywordBlind(
        onClickMore = {}
    )
}