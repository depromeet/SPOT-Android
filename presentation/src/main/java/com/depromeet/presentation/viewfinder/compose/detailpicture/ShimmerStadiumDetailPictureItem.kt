package com.depromeet.presentation.viewfinder.compose.detailpicture

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.presentation.extension.shimmerEffect

@Composable
fun ShimmerStadiumDetailPictureItem(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(SpotTheme.colors.backgroundWhite),

            ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.transferBlack01.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp)
                        .shimmerEffect()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .shimmerEffect()
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RectangleShape)
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RectangleShape)
                            .shimmerEffect()
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth(0.3f)
                        .clip(RectangleShape)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(25.dp)
                        .fillMaxWidth(0.7f)
                        .clip(RectangleShape)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth(0.5f)
                    .clip(RectangleShape)
                    .shimmerEffect()
            )
        }
    } else {
        content()
    }
}

@Preview
@Composable
private fun ShimmerStadiumDetailPictureItemPreview() {
    ShimmerStadiumDetailPictureItem(
        isLoading = true,
        content = {}
    )
}