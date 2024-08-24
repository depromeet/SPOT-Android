package com.dpm.presentation.viewfinder.compose.detailpicture

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.dpm.presentation.extension.noRippleClickable

@Composable
fun DetailReviewInteractionItems(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = SpotTheme.colors.transferBlack01,
                shape = RoundedCornerShape(80.dp)
            )
            .padding(
                vertical = 12.dp,
                horizontal = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_like_inactive),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(30.dp)
            )
        }
        Text(
            text = 10.toString(),
            style = SpotTheme.typography.label10,
            color = SpotTheme.colors.foregroundWhite
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_scrap),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(23.dp)
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}

@Preview
@Composable
private fun DetailReviewInteractionItemsPreview() {
    DetailReviewInteractionItems()
}