package com.depromeet.presentation.viewfinder.compose

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumPictureViewPager(
    context: Context,
    topReviewImages: List<BlockReviewResponse.TopReviewImagesResponse>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { topReviewImages.size })

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
    ) { page ->
        Card(
            modifier = Modifier.height(375.dp),
            shape = RectangleShape
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(topReviewImages.getOrNull(page)?.url)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = BrushPainter(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFE1E1E1),
                            Color(0x00E1E1E1),
                        )
                    )
                ),
                modifier = Modifier.fillMaxWidth(),
                onLoading = {

                },
                onSuccess = {

                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    text = "${topReviewImages.getOrNull(page)?.formattedNumber()}",
                    style = SpotTheme.typography.label11,
                    color = SpotTheme.colors.foregroundWhite,
                    modifier = Modifier
                        .padding(end = 16.dp, top = 16.dp)
                        .background(
                            color = SpotTheme.colors.transferBlack01,
                            shape = RoundedCornerShape(36.dp)
                        )
                        .padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        ),
                    textAlign = TextAlign.Center,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = "${page + 1}/${topReviewImages.size}",
                    style = SpotTheme.typography.label09,
                    color = SpotTheme.colors.foregroundWhite,
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp)
                        .background(
                            color = SpotTheme.colors.transferBlack01,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(
                            horizontal = 14.dp,
                            vertical = 6.dp
                        ),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun StadiumPictureViewPagerPreview() {
    StadiumPictureViewPager(
        context = LocalContext.current,
        topReviewImages = listOf(
            BlockReviewResponse.TopReviewImagesResponse(
                url = "",
                reviewId = 1,
                blockCode = "207",
                rowNumber = 1,
                seatNumber = 12
            ),
            BlockReviewResponse.TopReviewImagesResponse(
                url = "",
                reviewId = 1,
                blockCode = "207",
                rowNumber = 1,
                seatNumber = 12
            ),
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
