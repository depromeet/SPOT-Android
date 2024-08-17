package com.dpm.presentation.viewfinder.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dpm.designsystem.compose.ui.SpotTheme
import com.depromeet.presentation.R

@Composable
fun StadiumEmptyReviewContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = com.depromeet.designsystem.R.drawable.ic_empty_archiving),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.viewfinder_empty_review),
            style = SpotTheme.typography.title04,
            color = SpotTheme.colors.foregroundHeading,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.viewfinder_empty_review_description),
            style = SpotTheme.typography.body02,
            color = SpotTheme.colors.foregroundCaption,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun StadiumEmptyReviewContentPreview() {
    StadiumEmptyReviewContent(
        modifier = Modifier.background(SpotTheme.colors.backgroundWhite)
    )
}