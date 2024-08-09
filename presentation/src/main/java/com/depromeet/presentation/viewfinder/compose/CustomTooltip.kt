package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.domain.entity.request.viewfinder.RequestBlockReviewQuery
import com.depromeet.presentation.R

@Composable
fun CustomTooltip(
    triangleWidth: Dp = 12.dp,
    triangleHeight: Dp = 5.dp,
    reviewFilter: RequestBlockReviewQuery,
    modifier: Modifier = Modifier
) {
    if (reviewFilter.seatNumberIsEmpty() && reviewFilter.rowNumberIsEmpty()) {
        var tooltipWidth by remember {
            mutableStateOf(0f)
        }
        Column(
            modifier = modifier
        ) {
            Canvas(
                modifier = Modifier
                    .height(triangleHeight)
            ) {
                val trianglePath = Path().apply {
                    moveTo(tooltipWidth / 2, 0f) // 삼각형 꼭짓점
                    lineTo( // 오른쪽 아래 점
                        tooltipWidth / 2 + (triangleWidth.toPx() / 2),
                        triangleHeight.toPx()
                    )
                    lineTo( // 왼쪽 아래 점
                        tooltipWidth / 2 - (triangleWidth.toPx() / 2),
                        triangleHeight.toPx()
                    )
                    close()
                }

                drawPath(
                    path = trianglePath,
                    color = Color(0xE6000000)
                )
            }
            Box(modifier = Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    tooltipWidth = layoutCoordinates.size.width.toFloat()
                }
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
        reviewFilter = RequestBlockReviewQuery(
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
        reviewFilter = RequestBlockReviewQuery(
            rowNumber = 1,
            seatNumber = 12,
            year = null,
            month = null,
            page = 0,
            size = 10
        ),
    )
}