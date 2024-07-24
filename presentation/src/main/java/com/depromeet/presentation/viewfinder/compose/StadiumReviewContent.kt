package com.depromeet.presentation.viewfinder.compose

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.mapper.toKeyword

@Composable
fun StadiumReviewContent(
    context: Context,
    reviewContent: BlockReviewResponse.ReviewResponse,
    modifier: Modifier = Modifier,
    onClick: (reviewContent: BlockReviewResponse.ReviewResponse) -> Unit,
    onClickReport: () -> Unit
) {
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
                        .data(reviewContent.member.profileImage)
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
                    text = reviewContent.member.nickname,
                    fontSize = 12.sp,
                    color = Color(0xFF9F9F9F)
                )
                Spacer(modifier = Modifier.width(4.dp))
                LevelCard(
                    level = reviewContent.member.level
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_horizontal_dots),
                contentDescription = null,
                tint = Color(0xFF9F9F9F),
                modifier = Modifier.clickable {
                    onClickReport()
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.padding(start = 36.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${reviewContent.formattedNumber()}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF121212)
            )
            Text(
                text = "・${reviewContent.formattedDate()}",
                fontSize = 12.sp,
                color = Color(0xFF9F9F9F)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.padding(start = 36.dp),
        ) {
            items(reviewContent.images.size) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(reviewContent.images[it].url)
                        .build(),
                    contentDescription = null,
                    placeholder = ColorPainter(Color.LightGray),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onClick(reviewContent) }
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = reviewContent.content,
            fontSize = 14.sp,
            color = Color(0xFF121212),
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            modifier = Modifier.padding(start = 36.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        KeywordFlowRow(
            keywords = reviewContent.keywords.map { it.toKeyword() },
            modifier = Modifier.padding(start = 36.dp, end = 16.dp),
        )
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
            reviewContent = BlockReviewResponse.ReviewResponse(
                id = 1,
                dateTime = "2024-07-13",
                content = "전체적으로 좋은 경험이었습니다. 다음에 또 오고 싶어요!",
                images = listOf(
                    BlockReviewResponse.ReviewResponse.ReviewImageResponse(
                        id = 1,
                        url = "https://picsum.photos/200/300"
                    ),
                    BlockReviewResponse.ReviewResponse.ReviewImageResponse(
                        id = 1,
                        url = "https://picsum.photos/200/300"
                    ),
                ),
                member = BlockReviewResponse.ReviewResponse.ReviewMemberResponse(
                    "https://picsum.photos/200/300",
                    nickname = "엘지의 왕자",
                    level = 0
                ),
                stadium = BlockReviewResponse.ReviewResponse.ReviewStadiumResponse(
                    id = 1,
                    name = "서울 잠실 야구장"
                ),
                section = BlockReviewResponse.ReviewResponse.ReviewSectionResponse(
                    id = 1,
                    name = "오렌지석",
                    alias = "응원석"
                ),
                block = BlockReviewResponse.ReviewResponse.ReviewBlockResponse(
                    id = 1,
                    code = "207"
                ),
                row = BlockReviewResponse.ReviewResponse.ReviewRowResponse(
                    id = 1,
                    number = 1
                ),
                seat = BlockReviewResponse.ReviewResponse.ReviewSeatResponse(
                    id = 1,
                    seatNumber = 12
                ),
                keywords = listOf(
                    BlockReviewResponse.ReviewResponse.ReviewKeywordResponse(
                        id = 1,
                        content = "",
                        isPositive = false
                    ),
                    BlockReviewResponse.ReviewResponse.ReviewKeywordResponse(
                        id = 1,
                        content = "",
                        isPositive = false
                    ),
                    BlockReviewResponse.ReviewResponse.ReviewKeywordResponse(
                        id = 1,
                        content = "",
                        isPositive = false
                    )
                )
            ),
            onClick = {},
            onClickReport = {}
        )

    }
}