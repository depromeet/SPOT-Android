package com.dpm.presentation.viewfinder.compose

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dpm.domain.entity.response.viewfinder.BASE
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview
import com.dpm.domain.entity.response.viewfinder.base
import com.dpm.presentation.mapper.toKeyword
import com.dpm.presentation.util.KakaoUtils
import com.dpm.presentation.util.kakaoShareSeatFeedTitle
import com.dpm.presentation.util.seatFeed
import com.dpm.presentation.util.stadiumBlock
import com.dpm.presentation.util.toTitle
import com.dpm.presentation.viewfinder.StadiumDetailActivity
import com.dpm.presentation.viewfinder.uistate.StadiumDetailUiState
import com.dpm.presentation.viewfinder.viewmodel.StadiumDetailViewModel
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailScreen(
    emptyBlockName: String,
    isFirstShare: Boolean,
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
    viewModel: StadiumDetailViewModel = viewModel(),
    onClickReviewPicture: (id: Long, index: Int, title: String) -> Unit,
    onClickTopImage: (id: Long, index: Int, title: String) -> Unit,
    onClickSelectSeat: () -> Unit,
    onClickFilterMonthly: () -> Unit,
    onClickReport: () -> Unit,
    onClickGoBack: () -> Unit,
    onClickShare:() -> Unit = {},
    onRefresh: () -> Unit
) {
    var isMore by remember { mutableStateOf(false) }
    var isFirstShare by remember { mutableStateOf(isFirstShare) }
    val verticalScrollState = rememberLazyListState()
    val scrollState by viewModel.scrollState.collectAsStateWithLifecycle()
    val reviewFilter by viewModel.reviewFilter.collectAsStateWithLifecycle()
    val detailUiState by viewModel.detailUiState.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentIndex.collectAsStateWithLifecycle()


    LaunchedEffect(key1 = scrollState) {
        verticalScrollState.scrollToItem(0)
        viewModel.updateScrollState(false)
    }

    LaunchedEffect(key1 = verticalScrollState.firstVisibleItemScrollOffset) {
        if (verticalScrollState.firstVisibleItemScrollOffset != 0) {
            viewModel.updateScrollState(true)
        }
    }

    LaunchedEffect(key1 = verticalScrollState.firstVisibleItemIndex) {
        if (detailUiState is StadiumDetailUiState.ReviewsData) {
            val reviewsData = (detailUiState as StadiumDetailUiState.ReviewsData)
            if (verticalScrollState.firstVisibleItemIndex == reviewsData.reviews.size - 1 && reviewsData.hasNext) {
                viewModel.getBlockReviews()
            }
        }
    }

    LaunchedEffect(key1 = currentIndex) {
        if (currentIndex > 0) {
            verticalScrollState.scrollToItem(currentIndex + 1)
        }
    }

    detailUiState.let { uiState ->
        when (uiState) {
            is StadiumDetailUiState.Empty -> {
                StadiumEmptyContent(
                    blockNumber = emptyBlockName,
                    onGoBack = onClickGoBack
                )
            }

            is StadiumDetailUiState.Failed -> {
                ErrorScreen(
                    onRefresh = onRefresh
                )
            }

            is StadiumDetailUiState.Loading -> Unit
            is StadiumDetailUiState.ReviewsData -> {
                LazyColumn(
                    state = verticalScrollState,
                    modifier = modifier
                ) {
                    item(StadiumDetailActivity.STADIUM_HEADER) {
                        StadiumHeaderContent(
                            context = context,
                            isMore = isMore,
                            reviewFilter = reviewFilter,
                            topReviewImages = uiState.topReviewImages,
                            stadiumTitle = uiState.stadiumContent.stadiumName.trim(),
                            seatContent = uiState.stadiumContent.stadiumBlock(),
                            keywords = uiState.keywords.map { it.toKeyword() },
                            onChangeIsMore = { isMore = it },
                            onClickSelectSeat = onClickSelectSeat,
                            onCancelSeat = viewModel::clearSeat,
                            onClickTopImage = { id, index ->
                                onClickTopImage(id, index, uiState.stadiumContent.toTitle())
                            }
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }

                    stickyHeader {
                        StadiumViewReviewHeader(
                            reviewQuery = reviewFilter,
                            reviewCount = uiState.total,
                            onClickMonthly = onClickFilterMonthly,
                            onCancel = viewModel::clearMonth,
                            onClickDateTime = viewModel::updateSort,
                            onClickLikeCount = viewModel::updateSort
                        )
                    }

                    if (uiState.reviews.isEmpty()) {
                        item(StadiumDetailActivity.STADIUM_REVIEW_CONTENT) {
                            StadiumEmptyReviewContent()
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    } else {
                        items(
                            key = { index ->
                                uiState.reviews[index].id
                            },
                            count = uiState.reviews.size
                        ) { index ->
                            StadiumReviewContent(
                                context = context,
                                isFirstShare = isFirstShare,
                                firstReview = uiState.reviews[index] == uiState.reviews.firstOrNull(),
                                reviewContent = uiState.reviews[index],
                                onClick = { reviewContent, index ->
                                    onClickReviewPicture(
                                        reviewContent.id,
                                        index,
                                        uiState.stadiumContent.toTitle()
                                    )
                                },
                                onClickReport = onClickReport,
                                onClickLike = viewModel::updateLike,
                                onClickScrap = viewModel::updateScrap,
                                onClickShare = {
                                    onClickShare()
                                    isFirstShare = false
                                    KakaoUtils().share(
                                        context,
                                        seatFeed(
                                            title = uiState.kakaoShareSeatFeedTitle(index),
                                            description = "출처 : ${uiState.reviews[index].member.nickname}",
                                            imageUrl = uiState.reviews[index].images.firstOrNull()?.url
                                                ?: "",
                                            queryParams = mapOf(
                                                "stadiumId" to viewModel.stadiumId.toString(),
                                                "blockCode" to viewModel.blockCode
                                            )
                                        ),
                                        onSuccess = { sharingIntent ->
                                            context.startActivity(sharingIntent)
                                        },
                                        onFailure = {
                                            Timber.e("error : ${it.message}")
                                        }
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumDetailScreenPreview() {
    Box(modifier = Modifier.background(Color.White)) {
        StadiumDetailScreen(
            isFirstShare = true,
            emptyBlockName = "207",
            onClickReviewPicture = { _, _, _ -> },
            onClickSelectSeat = {},
            onClickFilterMonthly = {},
            onClickReport = {},
            onClickGoBack = {},
            onRefresh = {},
            onClickTopImage = { _, _, _ -> },
        )
    }
}