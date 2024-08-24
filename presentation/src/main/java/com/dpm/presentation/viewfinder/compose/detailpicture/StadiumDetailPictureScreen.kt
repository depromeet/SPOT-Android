package com.dpm.presentation.viewfinder.compose.detailpicture

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dpm.presentation.viewfinder.DetailReviewEntryPoint
import com.dpm.presentation.viewfinder.compose.detailpicture.main.StadiumDetailPictureTopScreen
import com.dpm.presentation.viewfinder.compose.detailpicture.top.StadiumDetailPictureMainScreen
import com.dpm.presentation.viewfinder.viewmodel.StadiumDetailViewModel

@Composable
fun StadiumDetailPictureScreen(
    context: Context = LocalContext.current,
    stadiumDetailViewModel: StadiumDetailViewModel = viewModel(),
    reviewId: Long,
    reviewIndex: Int,
    type: String,
    onClickScrap: (id: Long) -> Unit = {}
) {
    val reviews = stadiumDetailViewModel.detailUiState.collectAsStateWithLifecycle()
    val bottomPadding by stadiumDetailViewModel.bottomPadding.collectAsStateWithLifecycle()

    when (type) {
        DetailReviewEntryPoint.TOP_REVIEW.name -> {
            StadiumDetailPictureTopScreen(
                context = context,
                reviews = reviews.value,
                reviewId = reviewId,
                reviewIndex = reviewIndex,
                bottomPadding = bottomPadding,
                stadiumDetailViewModel = stadiumDetailViewModel,
                onClickScrap = onClickScrap
            )
        }

        DetailReviewEntryPoint.MAIN_REVIEW.name -> {
            StadiumDetailPictureMainScreen(
                context = context,
                reviews = reviews.value,
                reviewId = reviewId,
                reviewIndex = reviewIndex,
                bottomPadding = bottomPadding,
                stadiumDetailViewModel = stadiumDetailViewModel,
                onClickScrap = onClickScrap
            )
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