package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTooltip(
    modifier: Modifier = Modifier
) {
    Column {
        Canvas(
            modifier = Modifier,
            onDraw = {
                // 삼각형을 그릴 Path 생성
                val path = Path().apply {
                    moveTo(104.dp.toPx(), 0.dp.toPx()) // 삼각형의 꼭짓점
                    lineTo(110.dp.toPx(), 5.dp.toPx()) // 오른쪽 아래 점
                    lineTo(98.dp.toPx(), 5.dp.toPx()) // 왼쪽 아래 점
                    lineTo(104.dp.toPx(), 0.dp.toPx())
                }

                drawPath(
                    path = path,
                    color = Color(0xCC000000)
                )
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = modifier
                .background(Color(0xCC000000), shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = "열과 번을 선택해 빠르게 자리를 찾아보세요⚡",
                color = Color.White,
                fontSize = 11.sp
            )
        }
    }

}

@Preview
@Composable
fun CustomTooltipPreview() {
    CustomTooltip()
}