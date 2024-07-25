package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depromeet.domain.entity.request.viewfinder.BlockReviewRequestQuery
import com.depromeet.presentation.R

@Composable
fun StadiumSeatCheckBox(
    reviewFilter: BlockReviewRequestQuery,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = modifier
            .background(
                color = if (reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()) {
                    Color(0xFFFFFFFF)
                } else {
                    Color(0xFF121212)
                },
                shape = RoundedCornerShape(100.dp)
            )
            .border(
                1.dp,
                color = if (reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()) {
                    Color(0xFFE5E5E5)
                } else {
                    Color(0xFF121212)
                }, shape = RoundedCornerShape(100.dp)
            )
            .clickable(
                enabled = true,
                onClick = onClick
            )
            .padding(vertical = 10.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (reviewFilter.seatNumberIsEmpty()) {
                if (reviewFilter.rowNumberIsEmpty()) {
                    "좌석 선택"
                } else {
                    "${reviewFilter.rowNumber}열"
                }
            } else {
                "${reviewFilter.rowNumber}열 ${reviewFilter.seatNumber}번"
            },
            fontSize = 13.sp,
            color = if (reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()) {
                Color(0xFF121212)
            } else {
                Color(0xFFFFFFFF)
            }
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            painter = if (reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()) {
                painterResource(id = R.drawable.ic_down)
            } else {
                painterResource(id = R.drawable.ic_close)
            },
            contentDescription = null,
            tint = if (reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()) {
                Color(0xFF878787)
            } else {
                Color(0xFFFFFFFF)
            },
            modifier = Modifier
                .size(12.dp)
                .clickable {
                    if (reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()) {
                        onClick()
                    } else {
                        onCancel()
                    }
                }
        )
    }
}

@Preview
@Composable
private fun StadiumSeatCheckBoxPreview() {
    StadiumSeatCheckBox(
        reviewFilter = BlockReviewRequestQuery(),
        modifier = Modifier,
        onClick = {},
        onCancel = {}

    )
}

@Preview
@Composable
private fun StadiumSeatCheckBoxPreviewRowNumber() {
    StadiumSeatCheckBox(
        reviewFilter = BlockReviewRequestQuery(
            rowNumber = 1,
        ),
        modifier = Modifier,
        onClick = {},
        onCancel = {}
    )
}

@Preview
@Composable
private fun StadiumSeatCheckBoxPreviewSeatNumber() {
    StadiumSeatCheckBox(
        reviewFilter = BlockReviewRequestQuery(
            rowNumber = 1,
            seatNumber = 12
        ),
        modifier = Modifier,
        onClick = {},
        onCancel = {}
    )
}