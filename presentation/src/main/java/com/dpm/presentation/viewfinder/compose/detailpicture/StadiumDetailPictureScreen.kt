package com.dpm.presentation.viewfinder.compose.detailpicture

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dpm.presentation.util.KakaoUtils
import com.dpm.presentation.util.kakaoShareSeatFeedTitle
import com.dpm.presentation.util.seatFeed
import com.dpm.presentation.viewfinder.DetailReviewEntryPoint
import com.dpm.presentation.viewfinder.StadiumDetailActivity
import com.dpm.presentation.viewfinder.uistate.StadiumDetailUiState
import com.dpm.presentation.viewfinder.viewmodel.StadiumDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailPictureScreen(
    context: Context = LocalContext.current,
    stadiumDetailViewModel: StadiumDetailViewModel = viewModel(),
    reviewId: Long,
    reviewIndex: Int,
    type: String,
    modifier: Modifier = Modifier
) {
    val reviews = stadiumDetailViewModel.detailUiState.collectAsStateWithLifecycle()
    val bottomPadding by stadiumDetailViewModel.bottomPadding.collectAsStateWithLifecycle()

    when(type) {
        DetailReviewEntryPoint.TOP_REVIEW.name -> {
            reviews.value.let { uiState ->
                when (uiState) {
                    is StadiumDetailUiState.ReviewsData -> {
                        val initPage by remember {
                            mutableStateOf(uiState.topReviewImages.indexOfFirst { it.id == reviewId })
                        }

                        val pagerState = rememberPagerState(
                            pageCount = { uiState.topReviewImages.size },
                            initialPage = initPage
                        )

                        var pageIndex by remember {
                            mutableStateOf(0)
                        }

                        LaunchedEffect(key1 = pagerState) {
                            snapshotFlow { pagerState.currentPage }.collect {
                                pageIndex = it
                            }
                        }

                        StadiumDetailReviewViewPager(
                            context = context,
                            reviews = uiState.topReviewImages,
                            position = reviewIndex,
                            pagerState = pagerState,
                            pageIndex = pageIndex,
                            bottomPadding = bottomPadding,
                            modifier = modifier,
                            onClickShare = { imagePosition ->
                                KakaoUtils().share(
                                    context,
                                    seatFeed(
                                        title = uiState.kakaoShareSeatFeedTitle(pageIndex),
                                        description = "출처 : ${uiState.reviews[pageIndex].member.nickname}",
                                        imageUrl = uiState.reviews[pageIndex].images[imagePosition].url,
                                        queryParams = mapOf(
                                            "stadiumId" to stadiumDetailViewModel.stadiumId.toString(),
                                            "blockCode" to stadiumDetailViewModel.blockCode
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
        }
        DetailReviewEntryPoint.MAIN_REVIEW.name -> {
            reviews.value.let { uiState ->
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
                            hasNext = uiState.hasNext,
                            pagerState = pagerState,
                            pageIndex = pageIndex,
                            bottomPadding = bottomPadding,
                            modifier = modifier,
                            onLoadPaging = stadiumDetailViewModel::getBlockReviews,
                            onClickShare = { imagePosition ->
                                KakaoUtils().share(
                                    context,
                                    seatFeed(
                                        title = uiState.kakaoShareSeatFeedTitle(pageIndex),
                                        description = "출처 : ${uiState.reviews[pageIndex].member.nickname}",
                                        imageUrl = uiState.reviews[pageIndex].images[imagePosition].url,
                                        queryParams = mapOf(
                                            "stadiumId" to stadiumDetailViewModel.stadiumId.toString(),
                                            "blockCode" to stadiumDetailViewModel.blockCode
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
        }
    }


}

@Preview
@Composable
private fun StadiumDetailPictureScreenMainPreview() {
    StadiumDetailPictureScreen(
        context = LocalContext.current,
        reviewId = 1,
        reviewIndex = 0,
        type = DetailReviewEntryPoint.MAIN_REVIEW.name
    )
}

@Preview
@Composable
private fun StadiumDetailPictureScreenTopPreview() {
    StadiumDetailPictureScreen(
        context = LocalContext.current,
        reviewId = 1,
        reviewIndex = 0,
        type = DetailReviewEntryPoint.TOP_REVIEW.name
    )
}