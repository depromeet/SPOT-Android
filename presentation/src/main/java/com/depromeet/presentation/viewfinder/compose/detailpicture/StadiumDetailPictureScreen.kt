package com.depromeet.presentation.viewfinder.compose.detailpicture

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depromeet.presentation.viewfinder.compose.KeywordFlowRow
import com.depromeet.presentation.viewfinder.compose.LevelCard
import com.depromeet.presentation.viewfinder.sample.ReviewContent
import com.depromeet.presentation.viewfinder.sample.pictures
import com.depromeet.presentation.viewfinder.sample.reviewContents

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailPictureScreen(
    context: Context = LocalContext.current,
    reviews: List<ReviewContent>,
    modifier: Modifier = Modifier
) {
    var isMore by remember {
        mutableStateOf(false)
    }

    var isMoreButton by remember {
        mutableStateOf(false)
    }

    var isDimmed by remember {
        mutableStateOf(false)
    }

    val pagerState = rememberPagerState(pageCount = { reviews.size })

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            isMore = false
            isMoreButton = false
            isDimmed = false
        }
    }

    VerticalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) { page ->
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(reviews[page].images.getOrNull(0))
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
                    .blur(5.dp)
            )

            Column(
                modifier = Modifier
                    .zIndex(if (isDimmed) 2f else 3f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StadiumDetailPictureViewPager(
                    context = context,
                    pictures = pictures
                )
            }

            Column(
                modifier = Modifier
                    .zIndex(if (isDimmed) 3f else 2f)
                    .fillMaxSize()
                    .clickable(
                        enabled = isDimmed,
                        onClick = {
                            if (isDimmed) {
                                isDimmed = false
                                isMoreButton = true
                                isMore = false
                            }
                        }
                    )
                    .background(
                        color = if (isDimmed) {
                            Color.Black.copy(alpha = 0.6f)
                        } else {
                            Color.Black.copy(alpha = 0.5f)
                        }
                    )
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 30.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(reviews[page].profile)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = ColorPainter(Color.LightGray),
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = reviews[page].user,
                        fontSize = 12.sp,
                        color = Color(0xFF9F9F9F)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    LevelCard(level = reviews[page].level)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = reviews[page].seat,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = reviews[page].content,
                        fontSize = 14.sp,
                        maxLines = if (isMore) {
                            Int.MAX_VALUE
                        } else {
                            1
                        },
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        onTextLayout = {
                            if (!isMore) {
                                if (it.size.width > context.resources.displayMetrics.widthPixels * 0.75) {
                                    isMoreButton = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(if (isMoreButton) 0.8f else 1f)
                    )
                    if (isMoreButton) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "더보기",
                            fontSize = 13.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                isMoreButton = false
                                isMore = true
                                isDimmed = true
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                KeywordFlowRow(
                    keywords = reviews[page].keyword,
                    overflowIndex = if (isDimmed) {
                        Int.MAX_VALUE
                    } else {
                        2
                    },
                    isSelfExpanded = false,
                    onActionCallback = {
                        isMoreButton = false
                        isMore = true
                        isDimmed = true
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun StadiumDetailPictureScreenPreview() {
    StadiumDetailPictureScreen(
        context = LocalContext.current,
        reviews = reviewContents,
    )
}