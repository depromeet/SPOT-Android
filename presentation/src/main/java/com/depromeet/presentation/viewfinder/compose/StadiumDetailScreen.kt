package com.depromeet.presentation.viewfinder.compose

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.depromeet.presentation.viewfinder.sample.Stadium
import com.depromeet.presentation.viewfinder.sample.StadiumArea
import com.depromeet.presentation.viewfinder.sample.keywords
import com.depromeet.presentation.viewfinder.sample.review

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StadiumDetailScreen(
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier
) {
    var isMore by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
    ) {
        item {
            StadiumHeaderContent(
                context = context,
                isMore = isMore,
                stadium = Stadium(1, "서울 잠실 야구장", emptyList(), "", false),
                stadiumArea = StadiumArea("1루", 207, "오렌지석"),
                keywords = keywords,
                onChangeIsMore = { isMore = it}
            )
            Spacer(modifier = Modifier.height(30.dp))
        }

        stickyHeader {
            StadiumViewReviewHeader(
                reviewCount = review.count,
                onClickMonthly = {}
            )
        }

        if (review.reviewContents.isEmpty()) {
            item {
                StadiumEmptyReviewContent()
                Spacer(modifier = Modifier.height(40.dp))
            }
        } else {
            itemsIndexed(review.reviewContents) { _, reviewContent ->
                StadiumReviewContent(context = context, reviewContent = reviewContent)
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    }
}

@Preview(showBackground = true)
@Composable
private fun StadiumDetailScreenPreview() {
    Box(modifier = Modifier.background(Color.White)) {
        StadiumDetailScreen(
            context = LocalContext.current
        )
    }
}