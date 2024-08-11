package com.depromeet.presentation.viewfinder.compose.detailpicture

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.depromeet.designsystem.compose.ui.SpotTheme


@Composable
fun GradientBox(
    brushColor: Color,
    top: Boolean = false,
    bottom: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (top) {
        Box(
            modifier = modifier
                .height(94.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            brushColor,
                            Color.Transparent,
                        )
                    ),
                    alpha = 1f
                )
        )
    }

    if (bottom) {
        Box(
            modifier = modifier
                .height(94.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            brushColor,
                        )
                    ),
                    alpha = 1f
                )
        )
    }
}


@Preview
@Composable
private fun GradientBoxTopPreview() {
    GradientBox(
        top = true,
        brushColor = SpotTheme.colors.transferBlack01
    )
}

@Preview
@Composable
private fun GradientBoxBottomPreview() {
    GradientBox(
        bottom = true,
        brushColor = SpotTheme.colors.transferBlack01
    )
}