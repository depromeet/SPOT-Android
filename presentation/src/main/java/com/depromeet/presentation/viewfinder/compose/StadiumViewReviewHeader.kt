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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depromeet.domain.entity.request.viewfinder.BlockReviewRequestQuery

@Composable
fun StadiumViewReviewHeader(
    reviewQuery: BlockReviewRequestQuery,
    reviewCount: Long,
    modifier: Modifier = Modifier,
    onClickMonthly: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "시야 후기",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = reviewCount.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFAFAFAF)
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
                text = "최신순",
                fontSize = 13.sp,
                color = Color(0xFF7B7B7B)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumViewReviewHeaderPreview() {
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        StadiumViewReviewHeader(
            reviewQuery = BlockReviewRequestQuery(
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