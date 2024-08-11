package com.depromeet.presentation.viewfinder.compose.detailpicture

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.depromeet.domain.entity.response.viewfinder.ResponseBlockReview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailViewPagerLayer(
    context: Context,
    isDimmed: Boolean,
    verticalPagerState: PagerState,
    pictures: List<ResponseBlockReview.ResponseReview.ResponseReviewImage>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .zIndex(if (isDimmed) 2f else 3f)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StadiumDetailPictureViewPager(
            context = context,
            pictures = pictures,
            verticalPagerState = verticalPagerState,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun DetailViewPagerLayerPreview() {
    val pictures = listOf(
        ResponseBlockReview.ResponseReview.ResponseReviewImage(
            1,
            ""
        ),
        ResponseBlockReview.ResponseReview.ResponseReviewImage(
            1,
            ""
        ),
        ResponseBlockReview.ResponseReview.ResponseReviewImage(
            1,
            ""
        )
    )
    val pagerState = rememberPagerState {
        pictures.size
    }
    DetailViewPagerLayer(
        context = LocalContext.current,
        isDimmed = true,
        pictures = pictures,
        verticalPagerState = pagerState
    )
}