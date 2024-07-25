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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depromeet.core.state.UiState
import com.depromeet.presentation.mapper.toKeyword
import com.depromeet.presentation.viewfinder.compose.KeywordFlowRow
import com.depromeet.presentation.viewfinder.compose.LevelCard
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailPictureScreen(
    context: Context = LocalContext.current,
    stadiumDetailViewModel: StadiumDetailViewModel = viewModel(),
    reviewId: Long,
    modifier: Modifier = Modifier
) {
    val blockReviews by stadiumDetailViewModel.blockReviews.collectAsStateWithLifecycle()

    var isMore by remember {
        mutableStateOf(false)
    }

    var isDimmed by remember {
        mutableStateOf(false)
    }

    blockReviews.let { uiState ->
        when (uiState) {
            is UiState.Empty -> Unit
            is UiState.Failure -> Unit
            is UiState.Loading -> Unit
            is UiState.Success -> {
                val reviews = uiState.data.reviews
                val initPage = reviews.indexOfFirst { it.id == reviewId }
                val pagerState = rememberPagerState(
                    pageCount = { reviews.size },
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
                    reviews = reviews,
                    pagerState = pagerState,
                    modifier = modifier,
                    isDimmed = isDimmed,
                    isMore = isMore,
                    onChangeIsDimmed = {
                        isDimmed = it
                    },
                    onChangeIsMore = {
                        isMore = it
                    }
                )
            }
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