package com.dpm.presentation.viewfinder.compose.detailpicture

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailPictureViewPager(
    context: Context,
    likeCount: Long,
    verticalPagerState: PagerState,
    pictures: List<ResponseBlockReview.ResponseReview.ResponseReviewImage>,
    modifier: Modifier = Modifier,
    onClickLike: () -> Unit,
    onClickScrap: () -> Unit,
    onClickShare: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
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
                        .heightIn(max = ((context.resources.displayMetrics.heightPixels / context.resources.displayMetrics.density) * 0.58).dp)
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            DetailReviewInteractionItems(
                likeCount = likeCount,
                onClickLike = onClickLike,
                onClickScrap = onClickScrap,
                onClickShare = onClickShare
            )
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
        likeCount = 1,
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
        onClickLike = { },
        onClickScrap = { },
        onClickShare = { }
    )
}