package com.depromeet.presentation.viewfinder.compose.detailpicture

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.depromeet.designsystem.compose.ui.SpotTheme

@Composable
fun DetailGradientLayer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpotTheme.colors.colorB3000000),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        GradientBox(
            top = true,
            brushColor = SpotTheme.colors.transferBlack03
        )

        GradientBox(
            bottom = true,
            brushColor = SpotTheme.colors.transferBlack03
        )
    }
}


@Preview
@Composable
private fun DetailGradientLayerPreview() {
    DetailGradientLayer()
}