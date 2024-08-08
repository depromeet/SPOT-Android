package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.presentation.R
import com.depromeet.presentation.viewfinder.sample.Keyword

@Composable
fun StadiumKeywordContent(
    isMore: Boolean,
    keywords: List<Keyword>,
    modifier: Modifier = Modifier,
    onChangeIsMore: (isMore: Boolean) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        if (isMore) {
            repeat(keywords.size) {
                StadiumKeywordRow(
                    keyword = keywords[it],
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(
                    id = R.string.viewfinder_best_keyword_description_format,
                    keywords.size
                ),
                style = SpotTheme.typography.caption02,
                color = SpotTheme.colors.foregroundBodySebtext,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_up),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
                    .size(28.dp)
                    .clickable { onChangeIsMore(false) },
                tint = SpotTheme.colors.foregroundCaption
            )
            Spacer(modifier = Modifier.height(4.dp))
        } else {
            when {
                keywords.isEmpty() -> Unit

                keywords.size in 1..3 -> {
                    repeat(keywords.size) {
                        StadiumKeywordRow(
                            keyword = keywords[it],
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            id = R.string.viewfinder_best_keyword_description_format,
                            keywords.size
                        ),
                        style = SpotTheme.typography.caption02,
                        color = SpotTheme.colors.foregroundBodySebtext,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
                        Spacer(modifier = Modifier.height(4.dp))
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
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        SpotTheme.colors.foregroundWhite
                    )
                ),
                alpha = 1f
            )
            .clickable { onClickMore() }, contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_down),
            contentDescription = null,
            modifier = Modifier
                .padding(
                    top = 24.dp,
                    bottom = 16.dp,
                )
                .size(28.dp),
            tint = SpotTheme.colors.foregroundCaption
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
            keywords = listOf(
                Keyword("🙍‍서서 응원하는 존", 44, 1),
            )
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
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
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
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
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
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
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
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
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
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
                Keyword("🙍‍서서 응원하는 존", 44, 1),
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