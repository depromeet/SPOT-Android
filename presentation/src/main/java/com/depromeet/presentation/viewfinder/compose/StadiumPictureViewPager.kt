package com.depromeet.presentation.viewfinder.compose

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
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.presentation.viewfinder.sample.pictures

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumPictureViewPager(
    context: Context,
//    pictures: List<String>,
    pictures: List<BlockReviewResponse.HeaderResponse>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { pictures.size })

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
                    .data(pictures.getOrNull(page)?.url)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(Color.LightGray),
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
//                    text = "207블록 3열 12반",
                    text = "${pictures.getOrNull(page)?.content}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 16.dp, top = 16.dp)
                        .background(Color(0x40000000), shape = RoundedCornerShape(36.dp))
                        .padding(
                            horizontal = 8.dp,
                            vertical = 6.dp
                        ),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = "${page + 1}/${pictures.size}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp)
                        .background(Color(0x40000000), shape = RoundedCornerShape(4.dp))
                        .padding(
                            horizontal = 14.dp,
                            vertical = 6.dp
                        ),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
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
        pictures = listOf(
            BlockReviewResponse.HeaderResponse(
                url = "",
                content = "207블럭 3열 12번"
            ),
            BlockReviewResponse.HeaderResponse(
                url = "",
                content = "207블럭 3열 12번"
            ),
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
