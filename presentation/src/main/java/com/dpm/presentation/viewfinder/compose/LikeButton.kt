package com.dpm.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
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
import com.dpm.presentation.extension.noRippleClickable

@Composable
fun LikeButton(
    isLike: Boolean,
    likeCount: Long,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(
                color = SpotTheme.colors.backgroundWhite,
                shape = RoundedCornerShape(72.dp)
            )
            .border(
                width = 1.dp,
                color = if (isLike) SpotTheme.colors.actionEnabled else SpotTheme.colors.strokeTertiary,
                shape = RoundedCornerShape(72.dp)
            )
            .padding(
                horizontal = 13.dp,
                vertical = 8.dp
            )
            .noRippleClickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                id = if (isLike) R.drawable.ic_like_active else R.drawable.ic_like_inactive
            ),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "너무 도움돼요",
            style = SpotTheme.typography.label11,
            color = SpotTheme.colors.foregroundHeading
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = likeCount.toString(),
            style = SpotTheme.typography.label09,
            color = if (isLike) SpotTheme.colors.actionEnabled else SpotTheme.colors.foregroundCaption
        )
    }
}

@Preview
@Composable
private fun LikeButtonPreview() {
    LikeButton(
        isLike = true,
        likeCount = 1,
        onClick = {}
    )
}

@Preview
@Composable
private fun LikeButtonFalsePreview() {
    LikeButton(
        isLike = false,
        likeCount = 1,
        onClick = {}
    )
}