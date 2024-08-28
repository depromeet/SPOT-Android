package com.dpm.presentation.viewfinder.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.depromeet.designsystem.R

@Composable
fun ReviewContentBottom(
    isLike: Boolean,
    isScrap: Boolean,
    likeCount: Long,
    modifier: Modifier = Modifier,
    onClickLike: () -> Unit,
    onClickScrap: () -> Unit,
    onClickShare: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LikeButton(
            isLike = isLike,
            likeCount = likeCount,
            onClick = onClickLike
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClickScrap,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isScrap) R.drawable.ic_scrap_active_button else R.drawable.ic_scrap_inactive_button
                    ), contentDescription = null,
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(
                onClick = onClickShare,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_share_button
                    ), contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Preview
@Composable
private fun ReviewContentBottomPreview() {
    ReviewContentBottom(
        isLike = false,
        isScrap = false,
        likeCount = 0,
        onClickLike = {},
        onClickScrap = {},
        onClickShare = {}
    )
}

@Preview
@Composable
private fun ReviewContentBottomLikePreview() {
    ReviewContentBottom(
        isLike = true,
        isScrap = false,
        likeCount = 0,
        onClickLike = {},
        onClickScrap = {},
        onClickShare = {}
    )
}

@Preview
@Composable
private fun ReviewContentBottomScrapPreview() {
    ReviewContentBottom(
        isLike = false,
        isScrap = true,
        likeCount = 0,
        onClickLike = {},
        onClickScrap = {},
        onClickShare = {}
    )
}

@Preview
@Composable
private fun ReviewContentBottomLikeAndScrapPreview() {
    ReviewContentBottom(
        isLike = true,
        isScrap = true,
        likeCount = 0,
        onClickLike = {},
        onClickScrap = {},
        onClickShare = {}
    )
}