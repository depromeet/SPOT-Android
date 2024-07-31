package com.depromeet.presentation.viewfinder.compose

import android.content.Context
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.presentation.mapper.toKeyword
import com.depromeet.presentation.viewfinder.StadiumDetailActivity
import com.depromeet.presentation.viewfinder.uistate.StadiumDetailUiState
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailScreen(
    blockNumber: String,
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
    viewModel: StadiumDetailViewModel = viewModel(),
    onClickReviewPicture: (BlockReviewResponse.ReviewResponse, Int) -> Unit,
    onClickSelectSeat: () -> Unit,
    onClickFilterMonthly: () -> Unit,
    onClickReport: () -> Unit,
    onClickGoBack: () -> Unit
) {
    var isMore by remember { mutableStateOf(false) }
    val verticalScrollState = rememberLazyListState()
    val scrollState by viewModel.scrollState.collectAsStateWithLifecycle()
    val reviewFilter by viewModel.reviewFilter.collectAsStateWithLifecycle()
    val detailUiState by viewModel.detailUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = scrollState) {
        verticalScrollState.scrollToItem(0)
        viewModel.updateScrollState(false)
    }

    detailUiState.let { uiState ->
        when (uiState) {
            is StadiumDetailUiState.Empty -> {
                StadiumEmptyContent(
                    blockNumber = when (blockNumber) {
                        "exciting1" -> "1루 익사이팅"
                        "exciting3" -> "3루 익사이팅"
                        "premium" -> "프리미엄"
                        else -> blockNumber
                    },
                    onGoBack = onClickGoBack
                )
            }

            is StadiumDetailUiState.Failed -> Unit
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
                            stadiumTitle = uiState.stadiumContent.stadiumName,
                            seatContent = uiState.stadiumContent.formattedStadiumBlock(),
                            keywords = uiState.keywords.map { it.toKeyword() },
                            onChangeIsMore = { isMore = it },
                            onClickSelectSeat = onClickSelectSeat,
                            onCancelSeat = viewModel::clearSeat
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }

                    stickyHeader {
                        StadiumViewReviewHeader(
                            reviewQuery = reviewFilter,
                            reviewCount = uiState.total,
                            onClickMonthly = onClickFilterMonthly,
                            onCancel = viewModel::clearMonth
                        )
                    }
                    if (uiState.reviews.isEmpty()) {
                        item(StadiumDetailActivity.STADIUM_REVIEW_CONTENT) {
                            StadiumEmptyReviewContent()
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    } else {
                        if (verticalScrollState.firstVisibleItemIndex == uiState.reviews.size - 1 && !uiState.pageState) {
                            viewModel.updateQueryPage { query ->
                                viewModel.getBlockReviews(query = query)
                            }
                        }
                        items(
                            key = { index ->
                                uiState.reviews[index].id
                            },
                            count = uiState.reviews.size
                        ) { index ->
                            StadiumReviewContent(
                                context = context,
                                reviewContent = uiState.reviews[index],
                                onClick = onClickReviewPicture,
                                onClickReport = onClickReport
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
            blockNumber = "207",
            onClickReviewPicture = { _, _ -> },
            onClickSelectSeat = {},
            onClickFilterMonthly = {},
            onClickReport = {},
            onClickGoBack = {}
        )
    }
}