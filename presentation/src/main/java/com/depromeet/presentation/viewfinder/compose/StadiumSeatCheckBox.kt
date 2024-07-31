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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.domain.entity.request.viewfinder.BlockReviewRequestQuery
import com.depromeet.presentation.R
import com.depromeet.presentation.viewfinder.sample.review

@Composable
fun StadiumSeatCheckBox(
    reviewFilter: BlockReviewRequestQuery,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    val backgroundModifier = if (
        reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()
    ) {
        modifier.background(
            color = SpotTheme.colors.backgroundSecondary,
            shape = RoundedCornerShape(999.dp)
        )
    } else {
        modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    SpotTheme.colors.spotGreen2CD7A6,
                    SpotTheme.colors.spotGreen5DD281
                )
            ),
            shape = RoundedCornerShape(999.dp)
        )
    }

    Row(
        modifier = backgroundModifier
            .clickable(
                enabled = true,
                onClick = onClick
            )
            .padding(vertical = 10.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (reviewFilter.seatNumberIsEmpty()) {
                if (reviewFilter.rowNumberIsEmpty()) {
                    stringResource(id = R.string.viewfinder_selection_seat)
                } else {
                    stringResource(
                        id = R.string.viewfinder_seat_format,
                        reviewFilter.rowNumber.toString()
                    )
                }
            } else {
                stringResource(
                    id = R.string.viewfinder_seat_number_format,
                    reviewFilter.rowNumber.toString(),
                    reviewFilter.seatNumber.toString()
                )
            },
            style = SpotTheme.typography.label05,
            color = if (reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()) {
                SpotTheme.colors.foregroundBodySubtitle
            } else {
                SpotTheme.colors.backgroundWhite
            }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = if (reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()) {
                painterResource(id = R.drawable.ic_chevron_right)
            } else {
                painterResource(id = R.drawable.ic_x_close)
            },
            contentDescription = null,
            tint = if (reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()) {
                SpotTheme.colors.foregroundDisabled
            } else {
                SpotTheme.colors.foregroundWhite
            },
            modifier = Modifier
                .size(20.dp)
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
        reviewFilter = BlockReviewRequestQuery(
            rowNumber = null,
            seatNumber = null,
            year = null,
            month = null,
            page = 0,
            size = 10
        ),
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
            seatNumber = null,
            year = null,
            month = null,
            page = 0,
            size = 10
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
            seatNumber = 12,
            year = null,
            month = null,
            page = 0,
            size = 10
        ),
        modifier = Modifier,
        onClick = {},
        onCancel = {}
    )
}