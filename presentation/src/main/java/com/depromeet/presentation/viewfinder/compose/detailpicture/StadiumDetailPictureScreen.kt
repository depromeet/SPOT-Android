package com.depromeet.presentation.viewfinder.compose.detailpicture

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.depromeet.presentation.viewfinder.uistate.StadiumDetailUiState
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailPictureScreen(
    context: Context = LocalContext.current,
    stadiumDetailViewModel: StadiumDetailViewModel = viewModel(),
    reviewId: Long,
    modifier: Modifier = Modifier
) {
    val reviews = stadiumDetailViewModel.detailUiState.collectAsStateWithLifecycle()

    reviews.value.let { uiState ->
        when (uiState) {
            is StadiumDetailUiState.ReviewsData -> {
                val initPage = uiState.reviews.indexOfFirst { it.id == reviewId }

                var isMore by remember {
                    mutableStateOf(false)
                }

                var isDimmed by remember {
                    mutableStateOf(false)
                }

                val pagerState = rememberPagerState(
                    pageCount = { uiState.reviews.size },
                    initialPage = initPage
                )

                LaunchedEffect(key1 = pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect {
                        isMore = false
                        isDimmed = false
                    }
                }

                StadiumDetailReviewViewPager(
                    context = context,
                    reviews = uiState.reviews,
                    page = uiState.pageState,
                    pagerState = pagerState,
                    modifier = modifier,
                    isDimmed = isDimmed,
                    isMore = isMore,
                    onChangeIsDimmed = {
                        isDimmed = it
                    },
                    onChangeIsMore = {
                        isMore = it
                    },
                    onLoadPaging = {
                        stadiumDetailViewModel.updateQueryPage { query ->
                            stadiumDetailViewModel.getBlockReviews(query = query)
                        }
                    }
                )
            }

            else -> Unit
        }
    }
}

@Preview
@Composable
private fun StadiumDetailPictureScreenPreview() {
    StadiumDetailPictureScreen(
        context = LocalContext.current,
        reviewId = 1,
    )
}