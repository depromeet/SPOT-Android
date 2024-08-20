package com.dpm.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.domain.entity.request.viewfinder.RequestBlockReviewQuery
import com.depromeet.presentation.R
import com.dpm.presentation.extension.noRippleClickable

@Composable
fun StadiumViewReviewHeader(
    reviewCount: Long,
    reviewQuery: RequestBlockReviewQuery,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onClickMonthly: () -> Unit,
    onClickDateTime: (sortBy: String) -> Unit,
    onClickLikeCount: (sortBy: String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SpotTheme.colors.backgroundWhite)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.viewfinder_review_view),
                style = SpotTheme.typography.subtitle01,
                color = SpotTheme.colors.foregroundHeading
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = reviewCount.toString(),
                style = SpotTheme.typography.subtitle03,
                color = SpotTheme.colors.foregroundCaption
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MonthlyViewCard(
                month = reviewQuery.month,
                onClick = onClickMonthly,
                onCancel = onCancel
            )
            Row {
                Text(
                    text = stringResource(id = R.string.viewfinder_latest),
                    style = SpotTheme.typography.caption01,
                    color = when(reviewQuery.sortBy) {
                        "DATE_TIME" -> SpotTheme.colors.foregroundHeading
                        "LIKE_COUNT" -> SpotTheme.colors.foregroundCaption
                        else -> SpotTheme.colors.foregroundCaption
                    },
                    modifier = Modifier.noRippleClickable {
//                        if (reviewQuery.sortBy != "DATE_TIME") onClickDateTime("DATE_TIME")
                        onClickDateTime("DATE_TIME")
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(id = R.string.viewfinder_agree),
                    style = SpotTheme.typography.caption01,
                    color = when(reviewQuery.sortBy) {
                        "DATE_TIME" -> SpotTheme.colors.foregroundCaption
                        "LIKE_COUNT" -> SpotTheme.colors.foregroundHeading
                        else -> SpotTheme.colors.foregroundCaption
                    },
                    modifier = Modifier.noRippleClickable {
                        // 서버 공감순 추가 시 진행
                        // if (reviewQuery.sortBy != "LIKE_COUNT") onClickDateTime("LIKE_COUNT")
                        onClickLikeCount("DATE_TIME")
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumViewReviewHeaderPreview() {
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        StadiumViewReviewHeader(
            reviewQuery = RequestBlockReviewQuery(
                rowNumber = null,
                seatNumber = null,
                year = null,
                month = null,
                cursor = null,
                sortBy = "DATE_TIME",
                size = 10
            ),
            reviewCount = 100,
            onClickMonthly = {},
            onCancel = {},
            onClickDateTime = {},
            onClickLikeCount = {}
        )
    }
}