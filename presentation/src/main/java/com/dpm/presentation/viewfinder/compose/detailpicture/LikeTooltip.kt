package com.dpm.presentation.viewfinder.compose.detailpicture

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.presentation.util.MultiStyleText

@Composable
fun LikeTooltip(
    bias: Float,
    triangleWidth: Dp = 12.dp,
    triangleHeight: Dp = 5.dp,
    content: String,
    isNextPage: Boolean,
    modifier: Modifier = Modifier
) {
    var tooltipWidth by remember {
        mutableStateOf(0f)
    }
    var tooltipHeight by remember {
        mutableStateOf(0f)
    }

    var animated by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isNextPage) {
        animated = true
    }

    val scale by animateFloatAsState(
        targetValue = if (animated) 1f else 0.2f,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Column(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin.Center // 중앙을 기준으로 변환
            }
            .wrapContentSize(Alignment.Center) // Column을 중앙에 배치
    ) {
        Box(modifier = Modifier
            .onGloballyPositioned { layoutCoordinates ->
                tooltipWidth = layoutCoordinates.size.width.toFloat()
                tooltipHeight = layoutCoordinates.size.height.toFloat()
            }
            .background(
                color = SpotTheme.colors.backgroundWhite,
                shape = RoundedCornerShape(46.dp)
            )
            .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            MultiStyleText(
                style = SpotTheme.typography.caption04,
                textWithColors = arrayOf(
                    Pair(content.substring(0, 7), SpotTheme.colors.foregroundBodySubtitle),
                    Pair(content.substring(7, 11), SpotTheme.colors.actionEnabled),
                    Pair(
                        content.substring(11, content.length),
                        SpotTheme.colors.foregroundBodySubtitle
                    )
                )
            )
        }
        Canvas(
            modifier = Modifier
                .height(triangleHeight)
        ) {
            val trianglePath = Path().apply {
                moveTo(tooltipWidth * bias, triangleHeight.toPx()) // 삼각형 꼭짓점
                lineTo( // 오른쪽 위 점
                    tooltipWidth * bias + (triangleWidth.toPx() / 2),
                    0f
                )
                lineTo( // 왼쪽 위 점
                    tooltipWidth * bias - (triangleWidth.toPx() / 2),
                    0f
                )
                close()
            }

            drawPath(
                path = trianglePath,
                color = Color(0xFFFFFFFF)
            )
        }
    }
}


@Preview
@Composable
private fun LikeTooltipPreview() {
    LikeTooltip(
        bias = 0.85f,
        triangleWidth = 8.dp,
        triangleHeight = 4.dp,
        content = "유용했다면, 도움돼요를 눌러주세요!",
        isNextPage = false
    )
}