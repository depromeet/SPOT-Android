package com.depromeet.presentation.viewfinder.compose

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.domain.entity.request.viewfinder.BlockReviewRequestQuery
import com.depromeet.presentation.R

@Composable
fun CustomTooltip(
    reviewFilter: BlockReviewRequestQuery,
    modifier: Modifier = Modifier
) {
    if (reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()) {
        Column {
            Canvas(
                modifier = Modifier,
                onDraw = {
                    // 삼각형을 그릴 Path 생성
                    val path = Path().apply {
                        moveTo(125.dp.toPx(), 0.dp.toPx()) // 삼각형의 꼭짓점
                        lineTo(131.dp.toPx(), 5.dp.toPx()) // 오른쪽 아래 점
                        lineTo(119.dp.toPx(), 5.dp.toPx()) // 왼쪽 아래 점
                        lineTo(125.dp.toPx(), 0.dp.toPx())
                    }

                    drawPath(
                        path = path,
                        color = Color(0xE6000000)
                    )
                }
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = modifier
                    .background(
                        color = SpotTheme.colors.transferBlack03,
                        shape = RoundedCornerShape(46.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.viewfinder_tooltip_description),
                    style = SpotTheme.typography.label12,
                    color = SpotTheme.colors.foregroundWhite,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CustomTooltipPreview() {
    CustomTooltip(
        reviewFilter = BlockReviewRequestQuery(
            rowNumber = null,
            seatNumber = null,
            year = null,
            month = null,
            page = 0,
            size = 10
        ),

        )
}

@Preview
@Composable
private fun CustomTooltipSeatPreview() {
    CustomTooltip(
        reviewFilter = BlockReviewRequestQuery(
            rowNumber = 1,
            seatNumber = 12,
            year = null,
            month = null,
            page = 0,
            size = 10
        ),
    )
}