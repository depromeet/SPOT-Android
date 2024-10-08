package com.dpm.presentation.viewfinder.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.depromeet.presentation.R
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview
import com.dpm.presentation.extension.noRippleClickable
import com.dpm.presentation.mapper.toKeyword
import com.dpm.presentation.util.toBlockContent
import kotlinx.coroutines.launch

private enum class ReviewContentShowMoreState {
    SHOW, HIDE, NOTHING
}

@Composable
fun StadiumReviewContent(
    context: Context,
    isFirstShare: Boolean,
    firstReview: Boolean,
    reviewContent: ResponseBlockReview.ResponseReview,
    modifier: Modifier = Modifier,
    onClick: (reviewContent: ResponseBlockReview.ResponseReview, index: Int) -> Unit,
    onClickReport: () -> Unit,
    onClickLike: (id: Long) -> Unit = {},
    onClickScrap: (id: Long) -> Unit = {},
    onClickShare: () -> Unit
) {
    val minimumLineLength = 3
    var showMoreButtonState by remember {
        mutableStateOf(ReviewContentShowMoreState.NOTHING)
    }
    val scope = rememberCoroutineScope()

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(com.depromeet.designsystem.R.raw.lottie_like)
    )
    val lottieAnimatable = rememberLottieAnimatable()

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(reviewContent.member.profileImage.ifEmpty { com.depromeet.designsystem.R.drawable.ic_default_profile })
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
                    error = painterResource(id = com.depromeet.designsystem.R.drawable.ic_default_profile),
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = reviewContent.member.nickname,
                    style = SpotTheme.typography.caption02,
                    color = SpotTheme.colors.foregroundBodySebtext
                )
                Spacer(modifier = Modifier.width(4.dp))
                LevelCard(
                    level = reviewContent.member.level
                )
            }
            Icon(
                painter = painterResource(id = com.depromeet.designsystem.R.drawable.ic_dots_horizontal),
                contentDescription = null,
                tint = SpotTheme.colors.foregroundDisabled,
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable {
                        onClickReport()
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(start = 32.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = reviewContent.toBlockContent(),
                style = SpotTheme.typography.subtitle02,
                color = SpotTheme.colors.foregroundHeading,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = stringResource(
                    id = R.string.viewfinder_date_dot,
                    reviewContent.formattedDate()
                ),
                style = SpotTheme.typography.caption02,
                color = SpotTheme.colors.foregroundBodySebtext
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.padding(start = 32.dp),
        ) {
            items(reviewContent.images.size) { index ->
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(reviewContent.images[index].url)
                        .build(),
                    contentDescription = null,
                    placeholder = BrushPainter(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFE1E1E1),
                                Color(0x00E1E1E1),
                            )
                        )
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .noRippleClickable {
                            onClick(reviewContent, index)
                        }
                )
                Spacer(modifier = Modifier.width(4.dp))
                if (index == reviewContent.images.size - 1) {
                    Spacer(modifier = Modifier.width(32.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(if (reviewContent.content.isEmpty()) 4.dp else 16.dp))
        if (reviewContent.content.isNotEmpty()) {
            Text(
                text = reviewContent.content,
                style = SpotTheme.typography.body03,
                color = SpotTheme.colors.foregroundHeading,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (showMoreButtonState == ReviewContentShowMoreState.SHOW) {
                    Int.MAX_VALUE
                } else {
                    minimumLineLength
                },
                modifier = Modifier.padding(start = 32.dp, end = 16.dp),
                onTextLayout = {
                    if (it.lineCount > minimumLineLength - 1) {
                        if (it.isLineEllipsized(minimumLineLength - 1)) showMoreButtonState =
                            ReviewContentShowMoreState.HIDE
                    }
                }
            )
        }
        when (showMoreButtonState) {
            ReviewContentShowMoreState.HIDE -> {
                Spacer(modifier = Modifier.height(height = 4.dp))
                Text(
                    text = stringResource(id = R.string.viewfinder_more),
                    style = SpotTheme.typography.label10,
                    color = SpotTheme.colors.foregroundCaption,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .noRippleClickable {
                            showMoreButtonState = ReviewContentShowMoreState.SHOW
                        }
                        .padding(start = 32.dp)
                )
            }

            ReviewContentShowMoreState.SHOW -> {
                Spacer(modifier = Modifier.height(height = 4.dp))
                Text(
                    text = stringResource(id = R.string.viewfinder_fold),
                    style = SpotTheme.typography.label10,
                    color = SpotTheme.colors.foregroundCaption,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .noRippleClickable {
                            showMoreButtonState = ReviewContentShowMoreState.HIDE
                        }
                        .padding(start = 32.dp)
                )
            }

            ReviewContentShowMoreState.NOTHING -> Unit
        }
        Spacer(modifier = Modifier.height(12.dp))
        KeywordFlowRow(
            keywords = reviewContent.keywords.map { it.toKeyword() },
            overflowIndex = if (showMoreButtonState == ReviewContentShowMoreState.SHOW) {
                -1
            } else {
                2
            },
            isSelfExpanded = showMoreButtonState == ReviewContentShowMoreState.NOTHING,
            modifier = Modifier.padding(start = 32.dp, end = 16.dp),
            onActionCallback = {
                if (showMoreButtonState != ReviewContentShowMoreState.NOTHING) {
                    showMoreButtonState = ReviewContentShowMoreState.SHOW
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier) {
            ReviewContentBottom(
                isLike = reviewContent.isLike,
                isScrap = reviewContent.isScrap,
                likeCount = reviewContent.likesCount,
                modifier = Modifier.padding(start = 32.dp, end = 16.dp),
                onClickLike = {
                    if (!reviewContent.isLike) {
                        scope.launch {
                            lottieAnimatable.animate(
                                composition = composition,
                                clipSpec = LottieClipSpec.Frame(0, 1200),
                                initialProgress = 0f
                            )
                        }
                    }
                    onClickLike(reviewContent.id)
                },
                onClickScrap = {
                    onClickScrap(reviewContent.id)
                },
                onClickShare = onClickShare
            )
            Box(
                modifier = Modifier
                    .offset(y = (-35).dp)
                    .padding(start = 18.dp)
                    .size(72.dp)
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { lottieAnimatable.progress },
                    contentScale = ContentScale.FillHeight
                )
            }
            if (firstReview && isFirstShare) {
                Box(
                    modifier = Modifier
                        .offset(y = 45.dp)
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    ShareTooltip(bias = 0.875f, content = "카카오톡으로 같이가는 친구에게 공유하기")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumReviewContentPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        StadiumReviewContent(
            context = LocalContext.current,
            isFirstShare = false,
            firstReview = false,
            reviewContent = ResponseBlockReview.ResponseReview(
                id = 1,
                dateTime = "2023-03-01T19:00:00",
                content = "asdfsdfsafsfda",
                images = listOf(
                    ResponseBlockReview.ResponseReview.ResponseReviewImage(
                        id = 1,
                        url = "https://picsum.photos/200/300"
                    ),
                    ResponseBlockReview.ResponseReview.ResponseReviewImage(
                        id = 1,
                        url = "https://picsum.photos/200/300"
                    ),
                ),
                member = ResponseBlockReview.ResponseReview.ResponseReviewMember(
                    "https://picsum.photos/200/300",
                    nickname = "엘지의 왕자",
                    level = 0
                ),
                stadium = ResponseBlockReview.ResponseReview.ResponseReviewStadium(
                    id = 1,
                    name = "서울 잠실 야구장"
                ),
                section = ResponseBlockReview.ResponseReview.ResponseReviewSection(
                    id = 1,
                    name = "오렌지석",
                    alias = "응원석"
                ),
                block = ResponseBlockReview.ResponseReview.ResponseReviewBlock(
                    id = 1,
                    code = "207"
                ),
                row = ResponseBlockReview.ResponseReview.ResponseReviewRow(
                    id = 1,
                    number = 1
                ),
                seat = ResponseBlockReview.ResponseReview.ResponseReviewSeat(
                    id = 1,
                    seatNumber = 12
                ),
                keywords = listOf(
                    ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                        id = 1,
                        content = "",
                        isPositive = false
                    ),
                    ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                        id = 1,
                        content = "",
                        isPositive = false
                    ),
                    ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                        id = 1,
                        content = "",
                        isPositive = false
                    )
                ),
                isLike = false,
                isScrap = false,
                likesCount = 1,
                scrapsCount = 0,
                reviewType = ""
            ),
            onClick = { _, _ -> },
            onClickReport = {},
            onClickShare = {}
        )

    }
}