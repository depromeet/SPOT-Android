package com.depromeet.presentation.viewfinder.compose.detailpicture

import android.content.Context
import android.util.Log
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
import com.depromeet.presentation.viewfinder.uistate.StadiumDetailUiState
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailPictureScreen(
    context: Context = LocalContext.current,
    stadiumDetailViewModel: StadiumDetailViewModel = viewModel(),
    reviewId: Long,
    reviewIndex: Int,
    modifier: Modifier = Modifier
) {
    val reviews = stadiumDetailViewModel.detailUiState.collectAsStateWithLifecycle()

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

                LaunchedEffect(key1 = pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect {
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
                    pageState = uiState.pageState,
                    pagerState = pagerState,
                    modifier = modifier,
                    onLoadPaging = {
                        stadiumDetailViewModel.updateQueryPage { query ->
                            stadiumDetailViewModel.getBlockReviews(query = query)
                        }
                    },
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
        reviewIndex = 0
    )
}