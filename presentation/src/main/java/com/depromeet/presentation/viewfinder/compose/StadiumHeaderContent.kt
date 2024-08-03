package com.depromeet.presentation.viewfinder.compose

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.domain.entity.request.viewfinder.BlockReviewRequestQuery
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.presentation.viewfinder.sample.Keyword

@Composable
fun StadiumHeaderContent(
    context: Context,
    isMore: Boolean,
    seatContent: String,
    stadiumTitle: String,
    keywords: List<Keyword>,
    reviewFilter: BlockReviewRequestQuery,
    topReviewImages: List<BlockReviewResponse.TopReviewImagesResponse>,
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
            BlockReviewResponse.TopReviewImagesResponse(
                url = "",
                reviewId = 1,
                blockCode = "207",
                rowNumber = 1,
                seatNumber = 12
            ),
            BlockReviewResponse.TopReviewImagesResponse(
                url = "",
                reviewId = 1,
                blockCode = "207",
                rowNumber = 1,
                seatNumber = 12
            ),
        ),
        reviewFilter = BlockReviewRequestQuery(
            rowNumber = null,
            seatNumber = null,
            year = null,
            month = null,
            page = 0,
            size = 1
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
            BlockReviewResponse.TopReviewImagesResponse(
                url = "",
                reviewId = 1,
                blockCode = "207",
                rowNumber = 1,
                seatNumber = 12

            ),
            BlockReviewResponse.TopReviewImagesResponse(
                url = "",
                reviewId = 1,
                blockCode = "207",
                rowNumber = 1,
                seatNumber = 12
            ),
        ),
        reviewFilter = BlockReviewRequestQuery(
            rowNumber = 1,
            seatNumber = 12,
            year = null,
            month = null,
            page = 0,
            size = 1
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
