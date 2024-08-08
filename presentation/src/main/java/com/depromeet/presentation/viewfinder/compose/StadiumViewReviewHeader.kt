package com.depromeet.presentation.viewfinder.compose

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
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.domain.entity.request.viewfinder.RequestBlockReviewQuery
import com.depromeet.presentation.R

@Composable
fun StadiumViewReviewHeader(
    reviewCount: Long,
    reviewQuery: RequestBlockReviewQuery,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onClickMonthly: () -> Unit,
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
            Text(
                text = stringResource(id = R.string.viewfinder_latest),
                style = SpotTheme.typography.caption01,
                color = SpotTheme.colors.foregroundCaption
            )
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
                page = 0,
                size = 10
            ),
            reviewCount = 100,
            onClickMonthly = {},
            onCancel = {}
        )
    }
}