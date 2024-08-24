package com.dpm.presentation.viewfinder.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.presentation.util.MultiStyleText

@Composable
fun ShareTooltip(
    bias: Float,
    triangleWidth: Dp = 12.dp,
    triangleHeight: Dp = 5.dp,
    content: String,
    modifier: Modifier = Modifier
) {
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
                moveTo(tooltipWidth * bias, 0f) // 삼각형 꼭짓점
                lineTo( // 오른쪽 아래 점
                    tooltipWidth * bias + (triangleWidth.toPx() / 2),
                    triangleHeight.toPx()
                )
                lineTo( // 왼쪽 아래 점
                    tooltipWidth * bias - (triangleWidth.toPx() / 2),
                    triangleHeight.toPx()
                )
                close()
            }

            drawPath(
                path = trianglePath,
                color = Color(0xE6212124)
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
            .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            MultiStyleText(
                style = SpotTheme.typography.caption04,
                textWithColors = arrayOf(
                    Pair(content.substring(0, 4), SpotTheme.colors.actionEnabled),
                    Pair(content.substring(4, content.length), SpotTheme.colors.foregroundWhite)
                )
            )
        }
    }
}


@Preview
@Composable
private fun ShareTooltipPreview() {
    ShareTooltip(
        bias = 0.85f,
        triangleWidth = 8.dp,
        triangleHeight = 4.dp,
        content = "카카오톡으로 같이가는 친구에게 공유하기",
    )
}