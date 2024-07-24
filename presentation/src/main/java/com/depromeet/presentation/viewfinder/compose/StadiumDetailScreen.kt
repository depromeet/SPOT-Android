package com.depromeet.presentation.viewfinder.compose

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.presentation.mapper.toKeyword
import com.depromeet.presentation.viewfinder.StadiumDetailActivity
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailScreen(
    blockNumber: String,
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
    viewModel: StadiumDetailViewModel = viewModel(),
    onClickReviewPicture: (BlockReviewResponse.ReviewResponse) -> Unit,
    onClickSelectSeat: () -> Unit,
    onClickFilterMonthly: () -> Unit,
    onClickReport: () -> Unit,
    onClickGoBack: () -> Unit
) {
    var isMore by remember { mutableStateOf(false) }
    val scrollState by viewModel.scrollState.collectAsStateWithLifecycle()
    val verticalScrollState = rememberLazyListState()
    val blockReviews by viewModel.blockReviews.collectAsStateWithLifecycle()
    val reviewFilter by viewModel.reviewFilter.collectAsStateWithLifecycle()

    blockReviews.let { state ->
        when (state) {
            is UiState.Empty -> {
                StadiumEmptyContent(
                    blockNumber = blockNumber,
                    onGoBack = onClickGoBack
                )
            }

            is UiState.Failure -> Unit
            is UiState.Loading -> Unit
            is UiState.Success -> {
                LazyColumn(
                    state = verticalScrollState,
                    modifier = modifier
                ) {
                    item(StadiumDetailActivity.STADIUM_HEADER) {
                        StadiumHeaderContent(
                            context = context,
                            isMore = isMore,
                            reviewFilter = reviewFilter,
                            topReviewImages = state.data.topReviewImages,
                            stadiumTitle = state.data.formattedStadiumTitle(),
                            seatContent = state.data.formattedStadiumBlock(),
                            keywords = state.data.keywords.map { it.toKeyword() },
                            onChangeIsMore = { isMore = it },
                            onClickSelectSeat = onClickSelectSeat
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }

                    stickyHeader {
                        StadiumViewReviewHeader(
                            reviewCount = state.data.totalElements,
                            onClickMonthly = onClickFilterMonthly
                        )
                    }

                    if (state.data.reviews.isEmpty()) {
                        item(StadiumDetailActivity.STADIUM_REVIEW_CONTENT) {
                            StadiumEmptyReviewContent()
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    } else {
                        itemsIndexed(
                            items = state.data.reviews
                        ) { _, reviewContent ->
                            StadiumReviewContent(
                                context = context,
                                reviewContent = reviewContent,
                                onClick = onClickReviewPicture,
                                onClickReport = onClickReport
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }

        if (scrollState) {
            LaunchedEffect(key1 = Unit) {
                verticalScrollState.scrollToItem(0)
                viewModel.updateScrollState(false)
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
            onClickReviewPicture = {},
            onClickSelectSeat = {},
            onClickFilterMonthly = {},
            onClickReport = {},
            onClickGoBack = {}
        )
    }
}