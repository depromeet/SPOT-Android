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
    topReviewImages: List<ResponseBlockReview.ResponseReview>,
    modifier: Modifier = Modifier,
    onChangeIsMore: (Boolean) -> Unit,
    onClickSelectSeat: () -> Unit,
    onClickTopImage:(id: Long, index: Int) -> Unit,
    onCancelSeat: () -> Unit
) {
    Column(
        modifier = modifier.background(SpotTheme.colors.backgroundWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StadiumPictureViewPager(
            context = context,
            topReviewImages = topReviewImages,
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickTopImage
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
    val review = ResponseBlockReview.ResponseReview(
        id = 1,
        dateTime = "2023-03-01T19:00:00",
        content = "asdfsdfsafsfda",
        images = listOf(
            ResponseBlockReview.ResponseReview.ResponseReviewImage(
                id = 1,
                url = "https://picsum.photos/200/300"
            ),
            ResponseBlockReview.ResponseReview.ResponseReviewImage(
                id = 1,
                url = "https://picsum.photos/200/300"
            ),
        ),
        member = ResponseBlockReview.ResponseReview.ResponseReviewMember(
            "https://picsum.photos/200/300",
            nickname = "엘지의 왕자",
            level = 0
        ),
        stadium = ResponseBlockReview.ResponseReview.ResponseReviewStadium(
            id = 1,
            name = "서울 잠실 야구장"
        ),
        section = ResponseBlockReview.ResponseReview.ResponseReviewSection(
            id = 1,
            name = "오렌지석",
            alias = "응원석"
        ),
        block = ResponseBlockReview.ResponseReview.ResponseReviewBlock(
            id = 1,
            code = "207"
        ),
        row = ResponseBlockReview.ResponseReview.ResponseReviewRow(
            id = 1,
            number = 1
        ),
        seat = ResponseBlockReview.ResponseReview.ResponseReviewSeat(
            id = 1,
            seatNumber = 12
        ),
        keywords = listOf(
            ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                id = 1,
                content = "",
                isPositive = false
            ),
            ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                id = 1,
                content = "",
                isPositive = false
            ),
            ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                id = 1,
                content = "",
                isPositive = false
            )
        ),
        isLike = false,
        isScrap = false,
        likesCount = 1,
        scrapsCount = 0,
        reviewType = ""
    )
    StadiumHeaderContent(
        context = LocalContext.current,
        isMore = false,
        topReviewImages = listOf(
            review, review
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
        onClickTopImage = {_,_ ->},
        onCancelSeat = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun StadiumHeaderContentIsMorePreview() {
    val review = ResponseBlockReview.ResponseReview(
        id = 1,
        dateTime = "2023-03-01T19:00:00",
        content = "asdfsdfsafsfda",
        images = listOf(
            ResponseBlockReview.ResponseReview.ResponseReviewImage(
                id = 1,
                url = "https://picsum.photos/200/300"
            ),
            ResponseBlockReview.ResponseReview.ResponseReviewImage(
                id = 1,
                url = "https://picsum.photos/200/300"
            ),
        ),
        member = ResponseBlockReview.ResponseReview.ResponseReviewMember(
            "https://picsum.photos/200/300",
            nickname = "엘지의 왕자",
            level = 0
        ),
        stadium = ResponseBlockReview.ResponseReview.ResponseReviewStadium(
            id = 1,
            name = "서울 잠실 야구장"
        ),
        section = ResponseBlockReview.ResponseReview.ResponseReviewSection(
            id = 1,
            name = "오렌지석",
            alias = "응원석"
        ),
        block = ResponseBlockReview.ResponseReview.ResponseReviewBlock(
            id = 1,
            code = "207"
        ),
        row = ResponseBlockReview.ResponseReview.ResponseReviewRow(
            id = 1,
            number = 1
        ),
        seat = ResponseBlockReview.ResponseReview.ResponseReviewSeat(
            id = 1,
            seatNumber = 12
        ),
        keywords = listOf(
            ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                id = 1,
                content = "",
                isPositive = false
            ),
            ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                id = 1,
                content = "",
                isPositive = false
            ),
            ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                id = 1,
                content = "",
                isPositive = false
            )
        ),
        isLike = false,
        isScrap = false,
        likesCount = 1,
        scrapsCount = 0,
        reviewType = ""
    )
    StadiumHeaderContent(
        context = LocalContext.current,
        isMore = true,
        topReviewImages = listOf(
            review,
            review,
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
        onClickTopImage = {_,_ ->},
        onCancelSeat = {}
    )
}
