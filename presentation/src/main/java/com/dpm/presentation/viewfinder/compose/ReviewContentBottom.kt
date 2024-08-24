package com.dpm.presentation.viewfinder.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
            likeCount = likeCount,
            onClick = onClickLike
        )
        Row {
            IconButton(
                onClick = onClickScrap
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_scrap_inactive_button
                    ), contentDescription = null,
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(
                onClick = onClickShare
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
        likeCount = 0,
        onClickLike = {},
        onClickScrap = {},
        onClickShare = {}
    )
}