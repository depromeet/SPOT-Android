package com.depromeet.presentation.viewfinder.compose.detailpicture

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.presentation.mapper.toKeyword
import com.depromeet.presentation.viewfinder.compose.KeywordFlowRow
import com.depromeet.presentation.viewfinder.compose.LevelCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailReviewViewPager(
    context: Context,
    reviews: List<BlockReviewResponse.ReviewResponse>,
    pagerState: PagerState,
    isDimmed: Boolean,
    isMore: Boolean,
    modifier: Modifier = Modifier,
    onChangeIsDimmed: (isDimmed: Boolean) -> Unit,
    onChangeIsMore: (isMore: Boolean) -> Unit
) {
    val minimumLineLength = 1

    VerticalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) { page ->
        var showMoreButtonState by remember {
            mutableStateOf(false)
        }
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(reviews[page].images.getOrNull(0)?.url)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
                    .blur(5.dp)
            )

            Column(
                modifier = Modifier
                    .zIndex(if (isDimmed) 2f else 3f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StadiumDetailPictureViewPager(
                    context = context,
                    pictures = reviews[pagerState.currentPage].images
                )
            }

            Column(
                modifier = Modifier
                    .zIndex(if (isDimmed) 3f else 2f)
                    .fillMaxSize()
                    .clickable(
                        enabled = isDimmed,
                        onClick = {
                            if (isDimmed) {
                                onChangeIsDimmed(false)
                                onChangeIsMore(false)
                                showMoreButtonState = false
                            }
                        }
                    )
                    .background(
                        color = if (isDimmed) {
                            Color.Black.copy(alpha = 0.6f)
                        } else {
                            Color.Black.copy(alpha = 0.5f)
                        }
                    )
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 30.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(reviews[page].member.profileImage)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = ColorPainter(Color.LightGray),
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = reviews[page].member.nickname,
                        fontSize = 12.sp,
                        color = Color(0xFF9F9F9F)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    LevelCard(
                        level = reviews[page].member.level,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = reviews[page].formattedNumber(),
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = reviews[page].content,
                        fontSize = 14.sp,
                        maxLines = if (isMore) {
                            Int.MAX_VALUE
                        } else {
                            minimumLineLength
                        },
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        onTextLayout = {
                            if (it.lineCount > minimumLineLength - 1) {
                                if (it.isLineEllipsized(minimumLineLength - 1)) showMoreButtonState =
                                    true
                            }
                        },
                        modifier = if (showMoreButtonState) {
                            Modifier.fillMaxWidth(0.8f)
                        } else {
                            Modifier.wrapContentWidth()
                        }
                    )
                    if (showMoreButtonState) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "더보기",
                            fontSize = 13.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                showMoreButtonState = false
                                onChangeIsMore(true)
                                onChangeIsDimmed(true)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                KeywordFlowRow(
                    keywords = reviews[page].keywords.map { it.toKeyword() },
                    overflowIndex = if (isDimmed) {
                        Int.MAX_VALUE
                    } else {
                        2
                    },
                    isSelfExpanded = false,
                    onActionCallback = {
                        showMoreButtonState = false
                        onChangeIsMore(true)
                        onChangeIsDimmed(true)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun StadiumDetailReviewViewPagerPreview() {
    val reviews = listOf(
        BlockReviewResponse.ReviewResponse(
            id = 1,
            member = BlockReviewResponse.ReviewResponse.ReviewMemberResponse(
                "https://picsum.photos/600/400",
                "조관희",
                1
            ),
            stadium = BlockReviewResponse.ReviewResponse.ReviewStadiumResponse(
                1, "서울 잠실 야구장"
            ),
            section = BlockReviewResponse.ReviewResponse.ReviewSectionResponse(
                1, "오렌지석", "응원석"
            ),
            block = BlockReviewResponse.ReviewResponse.ReviewBlockResponse(
                id = 1, code = "207"
            ),
            row = BlockReviewResponse.ReviewResponse.ReviewRowResponse(
                id = 1, number = 1
            ),
            seat = BlockReviewResponse.ReviewResponse.ReviewSeatResponse(
                id = 1, seatNumber = 12
            ),
            dateTime = "2023-03-01T19:00:00",
            content = "좋은 경기였습니다!",
            images = listOf(
                BlockReviewResponse.ReviewResponse.ReviewImageResponse(
                    id = 1, "https://picsum.photos/600/400"
                ),
                BlockReviewResponse.ReviewResponse.ReviewImageResponse(
                    id = 2, "https://picsum.photos/600/400"
                ),
                BlockReviewResponse.ReviewResponse.ReviewImageResponse(
                    id = 3, "https://picsum.photos/600/400"
                )
            ),
            keywords = listOf(
                BlockReviewResponse.ReviewResponse.ReviewKeywordResponse(
                    id = 1, content = "좋아요", isPositive = true
                ),
                BlockReviewResponse.ReviewResponse.ReviewKeywordResponse(
                    id = 2, content = "싫어요", isPositive = false
                )
            )
        )
    )
    val pagerState = rememberPagerState(
        pageCount = { reviews.size },
    )
    StadiumDetailReviewViewPager(
        context = LocalContext.current,
        reviews = reviews,
        pagerState = pagerState,
        isDimmed = false,
        isMore = false,
        onChangeIsDimmed = {},
        onChangeIsMore = {}
    )
}