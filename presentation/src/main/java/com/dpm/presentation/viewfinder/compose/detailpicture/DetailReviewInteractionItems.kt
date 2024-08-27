package com.dpm.presentation.viewfinder.compose.detailpicture

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.depromeet.designsystem.R
import com.dpm.designsystem.compose.ui.SpotTheme

@Composable
fun DetailReviewInteractionItems(
    isScrap: Boolean,
    isLike: Boolean,
    likeCount: Long,
    modifier: Modifier = Modifier,
    onClickLike: () -> Unit,
    onClickScrap: () -> Unit,
    onClickShare: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(
                end = 12.dp
            )
            .background(
                color = SpotTheme.colors.transferBlack01,
                shape = RoundedCornerShape(80.dp)
            )
            .padding(
                vertical = 12.dp,
                horizontal = 12.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onClickLike, modifier.size(24.dp)) {
            Icon(
                painter = painterResource(id = if (isLike) R.drawable.ic_like_active else R.drawable.ic_like_inactive),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = likeCount.toString(),
            style = SpotTheme.typography.label10,
            color = if (isLike) SpotTheme.colors.actionEnabled else SpotTheme.colors.foregroundWhite
        )
        Spacer(modifier = Modifier.height(15.dp))
        IconButton(onClick = {
            onClickScrap()
        }, modifier.size(24.dp)) {
            if (isScrap) {
                Image(
                    painter = painterResource(id = R.drawable.ic_scrap_active),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_scrap),
                    contentDescription = null,
                    tint = SpotTheme.colors.foregroundWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        IconButton(onClick = onClickShare, modifier.size(24.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = null,
                tint = SpotTheme.colors.foregroundWhite,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
private fun DetailReviewInteractionItemsLikePreview() {
    DetailReviewInteractionItems(
        isScrap = false,
        isLike = true,
        likeCount = 1,
        onClickLike = {},
        onClickScrap = {},
        onClickShare = {}
    )
}

@Preview
@Composable
private fun DetailReviewInteractionItemsPreview() {
    DetailReviewInteractionItems(
        isScrap = true,
        isLike = false,
        likeCount = 1,
        onClickLike = {},
        onClickScrap = {},
        onClickShare = {}
    )
}