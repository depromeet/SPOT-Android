package com.depromeet.presentation.viewfinder.compose.detailpicture

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.model.content.CircleShape
import com.depromeet.presentation.viewfinder.sample.pictures

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailPictureViewPager(
    context: Context,
    pictures: List<String>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { pictures.size })

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
        ) { page ->
            Card(
                modifier = Modifier
                    .heightIn(max = 420.dp)
                    .fillMaxWidth(),
                shape = RectangleShape
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(pictures.getOrNull(page))
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                    placeholder = ColorPainter(Color.LightGray),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.White else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun StadiumDetailPictureViewPagerPreview() {
    StadiumDetailPictureViewPager(
        context = LocalContext.current,
        pictures = pictures
    )
}