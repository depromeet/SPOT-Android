package com.depromeet.presentation.viewfinder.compose

import android.content.Context
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depromeet.presentation.R
import com.depromeet.presentation.viewfinder.sample.ReviewContent
import com.depromeet.presentation.viewfinder.sample.reviewContents

@Composable
fun StadiumReviewContent(
    context: Context,
    reviewContent: ReviewContent,
    modifier: Modifier = Modifier,
    onClick: (reviewContent: ReviewContent) -> Unit,
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
                        .data(reviewContent.profile)
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
                    text = reviewContent.user,
                    fontSize = 12.sp,
                    color = Color(0xFF9F9F9F)
                )
                Spacer(modifier = Modifier.width(4.dp))
                LevelCard(level = reviewContent.level)
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
                text = "${reviewContent.seat}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF121212)
            )
            Text(
                text = "・${reviewContent.date}",
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
                        .data(reviewContent.images[it])
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
            modifier = Modifier.padding(start = 36.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        KeywordFlowRow(
            keywords = reviewContent.keyword,
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
            reviewContent = reviewContents[0],
            onClick = {},
            onClickReport = {}
        )
    }
}