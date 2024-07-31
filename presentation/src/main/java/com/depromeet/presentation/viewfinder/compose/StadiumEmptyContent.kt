package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.presentation.R

@Composable
fun StadiumEmptyContent(
    blockNumber: String,
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpotTheme.colors.backgroundWhite),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_review),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(
                id = R.string.viewfinder_empty_block_review_description,
                blockNumber
            ),
            style = SpotTheme.typography.title04,
            color = SpotTheme.colors.foregroundHeading,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(id = R.string.viewfinder_empty_block_description),
            style = SpotTheme.typography.body02,
            color = SpotTheme.colors.foregroundCaption,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onGoBack,
            shape = RoundedCornerShape(999.dp),
            border = BorderStroke(width = 1.dp, color = SpotTheme.colors.strokeTertiary),
            colors = ButtonDefaults.buttonColors(backgroundColor = SpotTheme.colors.backgroundWhite),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    vertical = 4.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.viewfinder_go_back),
                    style = SpotTheme.typography.label05,
                    color = SpotTheme.colors.foregroundBodySubtitle,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = com.depromeet.designsystem.R.drawable.ic_refresh),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = SpotTheme.colors.foregroundDisabled
                )
            }
        }
    }
}

@Preview
@Composable
private fun StadiumEmptyContentPreview() {
    StadiumEmptyContent(
        blockNumber = "207",
        onGoBack = {}
    )
}
