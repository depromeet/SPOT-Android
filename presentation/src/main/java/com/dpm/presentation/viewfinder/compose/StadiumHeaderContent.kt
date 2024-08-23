package com.dpm.presentation.viewfinder.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.depromeet.presentation.R
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.domain.entity.request.viewfinder.RequestBlockReviewQuery
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview
import com.dpm.presentation.viewfinder.sample.Keyword

@Composable
fun StadiumHeaderContent(
    context: Context,
    isMore: Boolean,
    seatContent: String,
    stadiumTitle: String,
    keywords: List<Keyword>,
    reviewFilter: RequestBlockReviewQuery,
    topReviewImages: List<ResponseBlockReview.ResponseTopReviewImages>,
    modifier: Modifier = Modifier,
    onChangeIsMore: (Boolean) -> Unit,
    onClickSelectSeat: () -> Unit,
    onCancelSeat: () -> Unit
) {
    Column(
        modifier = modifier.background(SpotTheme.colors.backgroundWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StadiumPictureViewPager(
            context = context,
            topReviewImages = topReviewImages,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        StadiumAreaText(
            stadium = stadiumTitle,
            seatContent = seatContent
        )
        Spacer(modifier = Modifier.height(10.dp))
        StadiumSeatCheckBox(
            reviewFilter = reviewFilter,
            onClick = onClickSelectSeat,
            onCancel = onCancelSeat
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            StadiumKeywordContent(
                isMore = isMore,
                keywords = keywords,
                onChangeIsMore = onChangeIsMore
            )
            CustomTooltip(
                content = stringResource(id = R.string.viewfinder_tooltip_description),
                reviewFilter = reviewFilter,
                modifier = Modifier.zIndex(1f)
            )
        }
        Divider(
            color = SpotTheme.colors.backgroundTertiary,
            thickness = 10.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun StadiumHeaderContentPreview() {
    StadiumHeaderContent(
        context = LocalContext.current,
        isMore = false,
        topReviewImages = listOf(
            ResponseBlockReview.ResponseTopReviewImages(
                url = "",
                reviewId = 1,
                blockCode = "207",
                rowNumber = 1,
                seatNumber = 12
            ),
            ResponseBlockReview.ResponseTopReviewImages(
                url = "",
                reviewId = 1,
                blockCode = "207",
                rowNumber = 1,
                seatNumber = 12
            ),
        ),
        reviewFilter = RequestBlockReviewQuery(
            rowNumber = null,
            seatNumber = null,
            year = null,
            month = null,
            cursor = null,
            sortBy = "DATE_TIME",
            size = 10
        ),
        stadiumTitle = "서울 잠실 야구장",
        seatContent = "오렌지석 207블럭",
        keywords = listOf(
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
        ),
        onChangeIsMore = {},
        onClickSelectSeat = {},
        onCancelSeat = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun StadiumHeaderContentIsMorePreview() {
    StadiumHeaderContent(
        context = LocalContext.current,
        isMore = true,
        topReviewImages = listOf(
            ResponseBlockReview.ResponseTopReviewImages(
                url = "",
                reviewId = 1,
                blockCode = "207",
                rowNumber = 1,
                seatNumber = 12

            ),
            ResponseBlockReview.ResponseTopReviewImages(
                url = "",
                reviewId = 1,
                blockCode = "207",
                rowNumber = 1,
                seatNumber = 12
            ),
        ),
        reviewFilter = RequestBlockReviewQuery(
            rowNumber = 1,
            seatNumber = 12,
            year = null,
            month = null,
            cursor = null,
            sortBy = "DATE_TIME",
            size = 10
        ), stadiumTitle = "서울 잠실 야구장",
        seatContent = "오렌지석 207블럭",
        keywords = listOf(
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
        ),
        onChangeIsMore = {},
        onClickSelectSeat = {},
        onCancelSeat = {}
    )
}
