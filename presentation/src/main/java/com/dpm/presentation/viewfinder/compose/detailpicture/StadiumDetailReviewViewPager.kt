package com.dpm.presentation.viewfinder.compose.detailpicture

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailReviewViewPager(
    context: Context,
    position: Int,
    pageState: Boolean,
    pagerState: PagerState,
    bottomPadding: Float,
    reviews: List<ResponseBlockReview.ResponseReview>,
    visited: List<Boolean>,
    modifier: Modifier = Modifier,
    onLoadPaging: () -> Unit,
) {
    if (pagerState.currentPage == reviews.size - 1 && !pageState) {
        onLoadPaging()
    }

    VerticalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxSize()
            .background(SpotTheme.colors.backgroundWhite)
    ) { page ->
        var showMoreButtonState by remember {
            mutableStateOf(false)
        }

        var isDimmed by remember {
            mutableStateOf(false)
        }

        var isMore by remember {
            mutableStateOf(false)
        }

        val verticalPagerState = rememberPagerState(
            pageCount = { reviews[page].images.size },
            initialPage = position
        )

        LaunchedEffect(key1 = Unit) {
            if (!visited[page]) {
                verticalPagerState.scrollToPage(0)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DetailBackgroundLayer(
                context = context,
                image = reviews[page].images.getOrNull(verticalPagerState.currentPage)?.url,
            )
            DetailGradientLayer()
            DetailViewPagerLayer(
                context = context,
                isDimmed = isDimmed,
                pictures = reviews[page].images,
                verticalPagerState = verticalPagerState,
            )
            DetailContentLayer(
                context = context,
                isMore = isMore,
                isDimmed = isDimmed,
                showMoreButtonState = showMoreButtonState,
                bottomPadding = bottomPadding,
                review = reviews[page],
                onChangeIsMore = {
                    isMore = it
                },
                onChangeIsDimmed = {
                    isDimmed = it
                },
                onChangeShowMoreButtonState = {
                    showMoreButtonState = it
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun StadiumDetailReviewViewPagerPreview() {
    val reviews = listOf(
        ResponseBlockReview.ResponseReview(
            id = 1,
            member = ResponseBlockReview.ResponseReview.ResponseReviewMember(
                "https://picsum.photos/600/400",
                "조관희",
                1
            ),
            stadium = ResponseBlockReview.ResponseReview.ResponseReviewStadium(
                1, "서울 잠실 야구장"
            ),
            section = ResponseBlockReview.ResponseReview.ResponseReviewSection(
                1, "오렌지석", "응원석"
            ),
            block = ResponseBlockReview.ResponseReview.ResponseReviewBlock(
                id = 1, code = "207"
            ),
            row = ResponseBlockReview.ResponseReview.ResponseReviewRow(
                id = 1, number = 1
            ),
            seat = ResponseBlockReview.ResponseReview.ResponseReviewSeat(
                id = 1, seatNumber = 12
            ),
            dateTime = "2023-03-01T19:00:00",
            content = "",
            images = listOf(
                ResponseBlockReview.ResponseReview.ResponseReviewImage(
                    id = 1, "https://picsum.photos/600/400"
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewImage(
                    id = 2, "https://picsum.photos/600/400"
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewImage(
                    id = 3, "https://picsum.photos/600/400"
                )
            ),
            keywords = listOf(
                ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                    id = 1, content = "좋아요", isPositive = true
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
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
        visited = emptyList(),
        position = 1,
        pageState = false,
        pagerState = pagerState,
        bottomPadding = 0f,
        onLoadPaging = {},
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun StadiumDetailReviewViewPagerMorePreview() {
    val reviews = listOf(
        ResponseBlockReview.ResponseReview(
            id = 1,
            member = ResponseBlockReview.ResponseReview.ResponseReviewMember(
                "https://picsum.photos/600/400",
                "조관희",
                1
            ),
            stadium = ResponseBlockReview.ResponseReview.ResponseReviewStadium(
                1, "서울 잠실 야구장"
            ),
            section = ResponseBlockReview.ResponseReview.ResponseReviewSection(
                1, "오렌지석", "응원석"
            ),
            block = ResponseBlockReview.ResponseReview.ResponseReviewBlock(
                id = 1, code = "207"
            ),
            row = ResponseBlockReview.ResponseReview.ResponseReviewRow(
                id = 1, number = 1
            ),
            seat = ResponseBlockReview.ResponseReview.ResponseReviewSeat(
                id = 1, seatNumber = 12
            ),
            dateTime = "2023-03-01T19:00:00",
            content = "좋은 경기였습니다! 경기였습니다!경기였습니다!경기였습니다!경기였습니다!경기였습니다!",
            images = listOf(
                ResponseBlockReview.ResponseReview.ResponseReviewImage(
                    id = 1, "https://picsum.photos/600/400"
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewImage(
                    id = 2, "https://picsum.photos/600/400"
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewImage(
                    id = 3, "https://picsum.photos/600/400"
                )
            ),
            keywords = listOf(
                ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                    id = 1, content = "좋아요", isPositive = true
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                    id = 2, content = "싫어요", isPositive = false
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                    id = 1, content = "좋아요", isPositive = true
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                    id = 2, content = "싫어요", isPositive = false
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                    id = 1, content = "좋아요", isPositive = true
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
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
        visited = emptyList(),
        position = 1,
        pageState = false,
        pagerState = pagerState,
        bottomPadding = 0f,
        onLoadPaging = {},
    )
}