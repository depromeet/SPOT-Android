package com.depromeet.presentation.viewfinder.compose.detailpicture

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.domain.entity.response.viewfinder.ResponseBlockReview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailPictureViewPager(
    context: Context,
    verticalPagerState: PagerState,
    pictures: List<ResponseBlockReview.ResponseReview.ResponseReviewImage>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier,
            state = verticalPagerState,
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(pictures.getOrNull(page)?.url)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
                placeholder = ColorPainter(Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = ((context.resources.displayMetrics.heightPixels / context.resources.displayMetrics.density) * 0.5).dp)
                    .clip(RectangleShape),

                )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(verticalPagerState.pageCount) { iteration ->
                if (verticalPagerState.currentPage == iteration) {
                    Box(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                color = SpotTheme.colors.actionEnabled
                            )
                            .size(
                                height = 6.dp,
                                width = 15.dp
                            )
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .clip(CircleShape)
                            .background(
                                color = SpotTheme.colors.backgroundPrimary
                            )
                            .size(6.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun StadiumDetailPictureViewPagerPreview() {
    val pagerState = rememberPagerState {
        1
    }
    StadiumDetailPictureViewPager(
        context = LocalContext.current,
        verticalPagerState = pagerState,
        pictures = listOf(
            ResponseBlockReview.ResponseReview.ResponseReviewImage(
                id = 1, url = ""
            ),
            ResponseBlockReview.ResponseReview.ResponseReviewImage(
                id = 1, url = ""
            ),
            ResponseBlockReview.ResponseReview.ResponseReviewImage(
                id = 1, url = ""
            )
        ),
    )
}