package com.dpm.presentation.viewfinder.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dpm.designsystem.compose.ui.SpotTheme
import com.depromeet.presentation.R

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = com.depromeet.designsystem.R.drawable.ic_upload_fail),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.error_server_content_title),
            style = SpotTheme.typography.title04,
            color = SpotTheme.colors.foregroundHeading
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.error_server_content_description),
            style = SpotTheme.typography.body02,
            color = SpotTheme.colors.foregroundCaption
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .background(
                    color = SpotTheme.colors.backgroundWhite,
                    shape = RoundedCornerShape(999.dp)
                )
                .border(
                    width = 1.dp,
                    color = SpotTheme.colors.strokeTertiary,
                    shape = RoundedCornerShape(999.dp)
                )
                .clickable {
                    onRefresh()
                }
                .padding(
                    vertical = 11.dp,
                    horizontal = 12.dp
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.error_reload),
                style = SpotTheme.typography.label05,
                color = SpotTheme.colors.foregroundBodySubtitle
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = com.depromeet.designsystem.R.drawable.ic_refresh),
                contentDescription = null,
                tint = SpotTheme.colors.foregroundDisabled,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    ErrorScreen(
        modifier = Modifier.background(SpotTheme.colors.backgroundWhite),
        onRefresh = {}
    )
}