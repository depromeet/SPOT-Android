package com.dpm.presentation.viewfinder.compose.detailpicture.top

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dpm.presentation.scheme.SchemeKey
import com.dpm.presentation.util.KakaoUtils
import com.dpm.presentation.util.kakaoShareSeatFeedTitle
import com.dpm.presentation.util.seatFeed
import com.dpm.presentation.viewfinder.compose.detailpicture.StadiumDetailReviewViewPager
import com.dpm.presentation.viewfinder.uistate.StadiumDetailUiState
import com.dpm.presentation.viewfinder.viewmodel.StadiumDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailPictureMainScreen(
    context: Context,
    reviewId: Long,
    reviewIndex: Int,
    bottomPadding: Float,
    isFirstLike: Boolean,
    uiState: StadiumDetailUiState,
    modifier: Modifier = Modifier,
    stadiumDetailViewModel: StadiumDetailViewModel = viewModel(),
    onClickLike: () -> Unit = {},
    onClickScrap: (id: Long) -> Unit = {},
    onClickShare: () -> Unit = {}
) {
    when (uiState) {
        is StadiumDetailUiState.ReviewsData -> {
            val visited = remember {
                mutableStateListOf(
                    *List(uiState.reviews.size) { false }.toTypedArray()
                )
            }

            if (uiState.reviews.size - visited.size > 0) {
                visited.addAll(List(uiState.reviews.size - visited.size) { false })
            }

            val initPage by remember {
                mutableStateOf(uiState.reviews.indexOfFirst { it.id == reviewId })
            }

            val pagerState = rememberPagerState(
                pageCount = { uiState.reviews.size },
                initialPage = initPage
            )

            var pageIndex by remember {
                mutableStateOf(0)
            }

            var isFirstLikeState by remember {
                mutableStateOf(isFirstLike)
            }

            var isNextPage by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(key1 = pagerState.isScrollInProgress) {
                isNextPage = !pagerState.isScrollInProgress
            }

            LaunchedEffect(key1 = pagerState) {
                snapshotFlow { pagerState.currentPage }.collect {
                    pageIndex = it
                    stadiumDetailViewModel.updateCurrentIndex(it)
                    if (visited[it]) return@collect

                    if (it == initPage) {
                        visited[it] = true
                    }

                    if (!visited[it]) {
                        visited[it] = true
                    }
                }
            }

            StadiumDetailReviewViewPager(
                context = context,
                reviews = uiState.reviews,
                visited = visited,
                position = reviewIndex,
                isFirstLike = isFirstLikeState,
                isNextPage = isNextPage,
                hasNext = uiState.hasNext,
                pagerState = pagerState,
                pageIndex = pageIndex,
                bottomPadding = bottomPadding,
                modifier = modifier,
                onLoadPaging = stadiumDetailViewModel::getBlockReviews,
                onClickLike = { id ->
                    onClickLike()
                    isFirstLikeState = false
                    stadiumDetailViewModel.updateLike(id)
                },
                onClickScrap = { id ->
                    onClickScrap(id)
                    stadiumDetailViewModel.updateScrap(id)
                },
                onClickShare = { imagePosition ->
                    onClickShare()
                    KakaoUtils().share(
                        context,
                        seatFeed(
                            title = uiState.kakaoShareSeatFeedTitle(pageIndex),
                            description = "출처 : ${uiState.reviews[pageIndex].member.nickname}",
                            imageUrl = uiState.reviews[pageIndex].images[imagePosition].url,
                            queryParams = mapOf(
                                SchemeKey.STADIUM_ID to stadiumDetailViewModel.stadiumId.toString(),
                                SchemeKey.BLOCK_CODE to stadiumDetailViewModel.blockCode
                            )
                        ),
                        onSuccess = { sharingIntent ->
                            context.startActivity(sharingIntent)
                        }
                    )
                }
            )
        }

        else -> Unit
    }
}

@Preview
@Composable
private fun StadiumDetailPictureMainScreenPreview() {
    StadiumDetailPictureMainScreen(
        context = LocalContext.current,
        reviewId = 1,
        reviewIndex = 0,
        bottomPadding = 0f,
        isFirstLike = false,
        uiState = StadiumDetailUiState.Empty,
    )
}