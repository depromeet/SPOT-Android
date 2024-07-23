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
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.presentation.viewfinder.sample.Keyword

@Composable
fun StadiumHeaderContent(
    context: Context,
    topReviewImages: List<BlockReviewResponse.TopReviewImagesResponse>,
    stadiumTitle: String,
    seatContent: String,
    isMore: Boolean,
    seat: String,
    keywords: List<Keyword>,
    modifier: Modifier = Modifier,
    onChangeIsMore: (Boolean) -> Unit,
    onClickSelectSeat: () -> Unit
) {
    Column(
        modifier = modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StadiumPictureViewPager(
            context = context,
            topReviewImages = topReviewImages,
//            pictures = pictures,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        StadiumAreaText(
            stadium = stadiumTitle,
            seatContent = seatContent
        )
        Spacer(modifier = Modifier.height(10.dp))

        StadiumSeatCheckBox(
            seat = seat,
            onClick = onClickSelectSeat
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
                seat = seat,
                modifier = Modifier.zIndex(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Divider(
            color = Color(0xFFF4F4F4),
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
        seat = "",
        stadiumTitle = "서울 잠실 야구장",
        seatContent = "오렌지석 207블럭",
//        stadium = Stadium(1, "서울 잠실 야구장", emptyList(), "", false),
//        stadiumArea = StadiumArea("1루", 207, "오렌지석"),
        keywords = listOf(
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
        ),
        onChangeIsMore = {},
        onClickSelectSeat = {}
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
        seat = "1열 12번",
        stadiumTitle = "서울 잠실 야구장",
        seatContent = "오렌지석 207블럭",
        keywords = listOf(
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
            Keyword(message = "서서 응원하는 존", like = 5, type = 0),
        ),
        onChangeIsMore = {},
        onClickSelectSeat = {}
    )
}
