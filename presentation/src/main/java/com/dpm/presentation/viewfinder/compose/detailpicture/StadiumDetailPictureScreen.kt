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
    reviewId: Long,
    reviewIndex: Int,
    type: String,
    isFirstLike: Boolean,
    context: Context = LocalContext.current,
    stadiumDetailViewModel: StadiumDetailViewModel = viewModel(),
    onClickLike: () -> Unit = {},
    onClickScrap: (isScrap: Boolean) -> Unit = {},
    onClickShare: () -> Unit = {}
) {
    val reviews = stadiumDetailViewModel.detailUiState.collectAsStateWithLifecycle()
    val bottomPadding by stadiumDetailViewModel.bottomPadding.collectAsStateWithLifecycle()

    when (type) {
        DetailReviewEntryPoint.TOP_REVIEW.name -> {
            StadiumDetailPictureTopScreen(
                context = context,
                uiState = reviews.value,
                reviewId = reviewId,
                reviewIndex = reviewIndex,
                bottomPadding = bottomPadding,
                isFirstLike = isFirstLike,
                stadiumDetailViewModel = stadiumDetailViewModel,
                onClickLike = onClickLike,
                onClickScrap = { id ->
                    onClickScrap(stadiumDetailViewModel.checkTopReviewScrap(id))
                },
                onClickShare = onClickShare
            )
        }

        DetailReviewEntryPoint.MAIN_REVIEW.name -> {
            StadiumDetailPictureMainScreen(
                context = context,
                uiState = reviews.value,
                reviewId = reviewId,
                reviewIndex = reviewIndex,
                bottomPadding = bottomPadding,
                isFirstLike = isFirstLike,
                stadiumDetailViewModel = stadiumDetailViewModel,
                onClickLike = onClickLike,
                onClickScrap = { id ->
                    onClickScrap(stadiumDetailViewModel.checkScrap(id))
                },
                onClickShare = onClickShare
            )
        }
    }
}

@Preview
@Composable
private fun StadiumDetailPictureScreenMainPreview() {
    StadiumDetailPictureScreen(
        isFirstLike = true,
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
        isFirstLike = true,
        context = LocalContext.current,
        reviewId = 1,
        reviewIndex = 0,
        type = DetailReviewEntryPoint.TOP_REVIEW.name
    )
}