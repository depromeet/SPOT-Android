package com.dpm.presentation.viewfinder.compose

import android.content.Context
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumPictureViewPager(
    context: Context,
    topReviewImages: List<ResponseBlockReview.ResponseTopReviewImages>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { topReviewImages.size })

    Box(
        modifier = modifier
            .height(375.dp)
            .fillMaxWidth()
    ) {
        HorizontalPager(
            modifier = modifier,
            state = pagerState,
        ) { page ->
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RectangleShape),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "${topReviewImages.getOrNull(pagerState.currentPage)?.formattedNumber()}",
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
                text = "${pagerState.currentPage + 1}/${topReviewImages.size}",
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

@Preview
@Composable
private fun StadiumPictureViewPagerPreview() {
    StadiumPictureViewPager(
        context = LocalContext.current,
        topReviewImages = listOf(
            ResponseBlockReview.ResponseTopReviewImages(
                url = "",
                reviewId = 1,
                blockCode = "207",
                rowNumber = 1,
                seatNumber = 12
            ),
            ResponseBlockReview.ResponseTopReviewImages(
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
