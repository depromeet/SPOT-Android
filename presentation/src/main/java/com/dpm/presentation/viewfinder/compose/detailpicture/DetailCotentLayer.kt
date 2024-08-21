package com.dpm.presentation.viewfinder.compose.detailpicture

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview
import com.depromeet.presentation.R
import com.dpm.presentation.extension.noRippleClickable
import com.dpm.presentation.mapper.toKeyword
import com.dpm.presentation.util.toBlockContent
import com.dpm.presentation.viewfinder.compose.KeywordFlowRow
import com.dpm.presentation.viewfinder.compose.LevelCard

@Composable
fun DetailContentLayer(
    context: Context,
    isMore: Boolean,
    isDimmed: Boolean,
    showMoreButtonState: Boolean,
    bottomPadding: Float,
    review: ResponseBlockReview.ResponseReview,
    modifier: Modifier = Modifier,
    onChangeIsMore: (Boolean) -> Unit,
    onChangeIsDimmed: (Boolean) -> Unit,
    onChangeShowMoreButtonState: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .zIndex(if (isDimmed) 3f else 2f)
            .fillMaxSize()
            .noRippleClickable(enabled = isDimmed) {
                if (isDimmed) {
                    onChangeIsMore(false)
                    onChangeIsDimmed(false)
                    onChangeShowMoreButtonState(false)
                }
            }
            .background(
                color = if (isDimmed) {
                    SpotTheme.colors.transferBlack01.copy(alpha = 0.6f)
                } else {
                    SpotTheme.colors.transferBlack01.copy(alpha = 0f)
                }
            )
            .padding(horizontal = 16.dp)
            .padding(bottom = (20 + bottomPadding).dp),
        verticalArrangement = Arrangement.Bottom) {
        DetailContentHeader(
            context = context,
            level = review.member.level,
            image = review.member.profileImage,
            nickname = review.member.nickname
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = review.toBlockContent(),
            style = SpotTheme.typography.subtitle02,
            color = SpotTheme.colors.foregroundWhite,
        )
        Spacer(modifier = Modifier.height(8.dp))
        DetailContentDescription(
            content = review.content,
            isMore = isMore,
            showMoreButtonState = showMoreButtonState,
            onChangeIsMore = onChangeIsMore,
            onChangeIsDimmed = onChangeIsDimmed,
            onChangeShowMoreButtonState = onChangeShowMoreButtonState
        )
        Spacer(modifier = Modifier.height(if (review.content.isEmpty()) 0.dp else 8.dp))
        KeywordFlowRow(
            keywords = review.keywords.map { it.toKeyword() },
            overflowIndex = if (isDimmed) {
                -1
            } else {
                2
            },
            isSelfExpanded = false,
            onActionCallback = {
                onChangeIsMore(true)
                onChangeIsDimmed(true)
                onChangeShowMoreButtonState(false)
            }
        )
    }
}

@Composable
fun DetailContentHeader(
    context: Context,
    level: Int,
    image: String,
    nickname: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(image.ifEmpty { com.depromeet.designsystem.R.drawable.ic_default_profile })
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
                .size(20.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = nickname,
            style = SpotTheme.typography.caption02,
            color = SpotTheme.colors.foregroundDisabled
        )
        Spacer(modifier = Modifier.width(4.dp))
        LevelCard(
            level = level,
        )
    }
}

@Composable
fun DetailContentDescription(
    content: String,
    isMore: Boolean,
    showMoreButtonState: Boolean,
    minimumLineLength: Int = 1,
    modifier: Modifier = Modifier,
    onChangeIsMore: (Boolean) -> Unit,
    onChangeIsDimmed: (Boolean) -> Unit,
    onChangeShowMoreButtonState: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (content.isNotEmpty()) {
            Text(
                text = content,
                style = SpotTheme.typography.body03,
                maxLines = if (isMore) {
                    Int.MAX_VALUE
                } else {
                    minimumLineLength
                },
                overflow = TextOverflow.Ellipsis,
                color = SpotTheme.colors.foregroundWhite,
                onTextLayout = {
                    if (it.lineCount > minimumLineLength - 1) {
                        if (it.isLineEllipsized(minimumLineLength - 1)) onChangeShowMoreButtonState(
                            true
                        )
                    }
                },
                modifier = if (showMoreButtonState) {
                    Modifier.fillMaxWidth(0.8f)
                } else {
                    Modifier.wrapContentWidth()
                }
            )
        }
        if (showMoreButtonState) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.viewfinder_more),
                style = SpotTheme.typography.label10,
                color = SpotTheme.colors.foregroundDisabled,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.noRippleClickable {
                    onChangeIsMore(true)
                    onChangeIsDimmed(true)
                    onChangeShowMoreButtonState(false)
                }
            )
        }
    }
}


@Preview
@Composable
private fun DetailContentLayerPreview() {
    val review =
        ResponseBlockReview.ResponseReview(
            id = 1,
            member = ResponseBlockReview.ResponseReview.ResponseReviewMember(
                "https://picsum.photos/600/400",
                "조관희",
                1
            ),
            stadium = ResponseBlockReview.ResponseReview.ResponseReviewStadium(
                1, "서울 잠실 야구장"
            ),
            section = ResponseBlockReview.ResponseReview.ResponseReviewSection(
                1, "오렌지석", "응원석"
            ),
            block = ResponseBlockReview.ResponseReview.ResponseReviewBlock(
                id = 1, code = "207"
            ),
            row = ResponseBlockReview.ResponseReview.ResponseReviewRow(
                id = 1, number = 1
            ),
            seat = ResponseBlockReview.ResponseReview.ResponseReviewSeat(
                id = 1, seatNumber = 12
            ),
            dateTime = "2023-03-01T19:00:00",
            content = "",
            images = listOf(
                ResponseBlockReview.ResponseReview.ResponseReviewImage(
                    id = 1, "https://picsum.photos/600/400"
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewImage(
                    id = 2, "https://picsum.photos/600/400"
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewImage(
                    id = 3, "https://picsum.photos/600/400"
                )
            ),
            keywords = listOf(
                ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                    id = 1, content = "좋아요", isPositive = true
                ),
                ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
                    id = 2, content = "싫어요", isPositive = false
                )
            )
        )
    DetailContentLayer(
        context = LocalContext.current,
        review = review,
        isMore = true,
        isDimmed = true,
        showMoreButtonState = true,
        bottomPadding = 0f,
        onChangeIsMore = {},
        onChangeIsDimmed = {},
        onChangeShowMoreButtonState = {}
    )
}

@Preview
@Composable
private fun DetailContentHeaderPreview() {
    DetailContentHeader(
        context = LocalContext.current,
        level = 1,
        image = "",
        nickname = "조관희"
    )
}

@Preview
@Composable
private fun DetailContentDescriptionPreview() {
    DetailContentDescription(
        content = "설명 설명",
        isMore = true,
        showMoreButtonState = true,
        onChangeIsMore = { },
        onChangeIsDimmed = { },
        onChangeShowMoreButtonState = {}
    )
}
