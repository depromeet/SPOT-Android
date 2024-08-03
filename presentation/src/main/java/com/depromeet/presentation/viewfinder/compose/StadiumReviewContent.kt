package com.depromeet.presentation.viewfinder.compose

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.extension.noRippleClickable
import com.depromeet.presentation.mapper.toKeyword

@Composable
fun StadiumReviewContent(
    context: Context,
    reviewContent: BlockReviewResponse.ReviewResponse,
    modifier: Modifier = Modifier,
    onClick: (reviewContent: BlockReviewResponse.ReviewResponse, index: Int) -> Unit,
    onClickReport: () -> Unit
) {
    val minimumLineLength = 3
    var showMoreButtonState by remember {
        mutableStateOf(-1)
    }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(reviewContent.member.profileImage.ifEmpty { com.depromeet.designsystem.R.drawable.ic_default_profile })
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = BrushPainter(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFE1E1E1),
                                Color(0x00E1E1E1),
                            )
                        )
                    ),
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = reviewContent.member.nickname,
                    style = SpotTheme.typography.caption02,
                    color = SpotTheme.colors.foregroundBodySebtext
                )
                Spacer(modifier = Modifier.width(4.dp))
                LevelCard(
                    level = reviewContent.member.level
                )
            }
            Icon(
                painter = painterResource(id = com.depromeet.designsystem.R.drawable.ic_dots_horizontal),
                contentDescription = null,
                tint = SpotTheme.colors.foregroundDisabled,
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable {
                        onClickReport()
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(start = 32.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = reviewContent.formattedNumber(),
                style = SpotTheme.typography.subtitle02,
                color = SpotTheme.colors.foregroundHeading,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = stringResource(
                    id = R.string.viewfinder_date_dot,
                    reviewContent.formattedDate()
                ),
                style = SpotTheme.typography.caption02,
                color = SpotTheme.colors.foregroundBodySebtext
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.padding(start = 32.dp),
        ) {
            items(reviewContent.images.size) { index ->
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(reviewContent.images[index].url)
                        .build(),
                    contentDescription = null,
                    placeholder = BrushPainter(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFE1E1E1),
                                Color(0x00E1E1E1),
                            )
                        )
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .noRippleClickable {
                            onClick(reviewContent, index)
                        }
                )
                Spacer(modifier = Modifier.width(4.dp))
                if (index == reviewContent.images.size - 1) {
                    Spacer(modifier = Modifier.width(32.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(if (reviewContent.content.isEmpty()) 4.dp else 16.dp))
        if (reviewContent.content.isNotEmpty()) {
            Text(
                text = reviewContent.content,
                style = SpotTheme.typography.body03,
                color = SpotTheme.colors.foregroundHeading,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (showMoreButtonState == 1) {
                    Int.MAX_VALUE
                } else {
                    minimumLineLength
                },
                modifier = Modifier.padding(start = 32.dp, end = 16.dp),
                onTextLayout = {
                    if (it.lineCount > minimumLineLength - 1) {
                        if (it.isLineEllipsized(minimumLineLength - 1)) showMoreButtonState = 0
                    }
                }
            )
        }
        when (showMoreButtonState) {
            0 -> {
                Spacer(modifier = Modifier.height(height = 4.dp))
                Text(
                    text = "더보기",
                    style = SpotTheme.typography.label10,
                    color = SpotTheme.colors.foregroundCaption,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .noRippleClickable {
                            showMoreButtonState = 1
                        }
                        .padding(start = 32.dp)
                )
            }

            1 -> {
                Spacer(modifier = Modifier.height(height = 4.dp))
                Text(
                    text = "접기",
                    style = SpotTheme.typography.label10,
                    color = SpotTheme.colors.foregroundCaption,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .noRippleClickable {
                            showMoreButtonState = 0
                        }
                        .padding(start = 32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        KeywordFlowRow(
            keywords = reviewContent.keywords.map { it.toKeyword() },
            modifier = Modifier.padding(start = 32.dp, end = 16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumReviewContentPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        StadiumReviewContent(
            context = LocalContext.current,
            reviewContent = BlockReviewResponse.ReviewResponse(
                id = 1,
                dateTime = "2023-03-01T19:00:00",
                content = "asdfsdfsafsfda",
                images = listOf(
                    BlockReviewResponse.ReviewResponse.ReviewImageResponse(
                        id = 1,
                        url = "https://picsum.photos/200/300"
                    ),
                    BlockReviewResponse.ReviewResponse.ReviewImageResponse(
                        id = 1,
                        url = "https://picsum.photos/200/300"
                    ),
                ),
                member = BlockReviewResponse.ReviewResponse.ReviewMemberResponse(
                    "https://picsum.photos/200/300",
                    nickname = "엘지의 왕자",
                    level = 0
                ),
                stadium = BlockReviewResponse.ReviewResponse.ReviewStadiumResponse(
                    id = 1,
                    name = "서울 잠실 야구장"
                ),
                section = BlockReviewResponse.ReviewResponse.ReviewSectionResponse(
                    id = 1,
                    name = "오렌지석",
                    alias = "응원석"
                ),
                block = BlockReviewResponse.ReviewResponse.ReviewBlockResponse(
                    id = 1,
                    code = "207"
                ),
                row = BlockReviewResponse.ReviewResponse.ReviewRowResponse(
                    id = 1,
                    number = 1
                ),
                seat = BlockReviewResponse.ReviewResponse.ReviewSeatResponse(
                    id = 1,
                    seatNumber = 12
                ),
                keywords = listOf(
                    BlockReviewResponse.ReviewResponse.ReviewKeywordResponse(
                        id = 1,
                        content = "",
                        isPositive = false
                    ),
                    BlockReviewResponse.ReviewResponse.ReviewKeywordResponse(
                        id = 1,
                        content = "",
                        isPositive = false
                    ),
                    BlockReviewResponse.ReviewResponse.ReviewKeywordResponse(
                        id = 1,
                        content = "",
                        isPositive = false
                    )
                )
            ),
            onClick = { _, _ -> },
            onClickReport = {}
        )

    }
}