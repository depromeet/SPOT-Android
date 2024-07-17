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
import com.depromeet.presentation.viewfinder.StadiumDetailActivity
import com.depromeet.presentation.viewfinder.sample.ReviewContent
import com.depromeet.presentation.viewfinder.sample.Stadium
import com.depromeet.presentation.viewfinder.sample.StadiumArea
import com.depromeet.presentation.viewfinder.sample.review
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailScreen(
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
    viewModel: StadiumDetailViewModel = viewModel(),
    onClickReviewPicture: (ReviewContent) -> Unit,
    onClickSelectSeat: () -> Unit,
    onClickFilterMonthly: () -> Unit,
    onClickReport: () -> Unit
) {
    var isMore by remember { mutableStateOf(false) }
    val scrollState by viewModel.scrollState.collectAsStateWithLifecycle()
    val verticalScrollState = rememberLazyListState()
    val blockReviews by viewModel.blockReviews.collectAsStateWithLifecycle()

    blockReviews.let { state ->
        when (state) {
            is UiState.Empty -> Unit
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
                            stadium = Stadium(1, "서울 잠실 야구장", emptyList(), "", false),
                            stadiumArea = StadiumArea("1루", 207, "오렌지석"),
                            keywords = state.data.keywords,
                            onChangeIsMore = { isMore = it },
                            onClickSelectSeat = onClickSelectSeat
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }

                    stickyHeader {
                        StadiumViewReviewHeader(
                            reviewCount = state.data.totalCount,
                            onClickMonthly = onClickFilterMonthly
                        )
                    }

                    if (review.reviewContents.isEmpty()) {
                        item(StadiumDetailActivity.STADIUM_REVIEW_CONTENT) {
                            StadiumEmptyReviewContent()
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    } else {
                        itemsIndexed(
                            items = review.reviewContents
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
            onClickReviewPicture = {},
            onClickSelectSeat = {},
            onClickFilterMonthly = {},
            onClickReport = {}
        )
    }
}