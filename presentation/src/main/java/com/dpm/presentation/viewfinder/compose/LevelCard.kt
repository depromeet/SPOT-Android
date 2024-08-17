package com.dpm.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dpm.designsystem.compose.ui.SpotTheme
import com.depromeet.presentation.R

@Composable
fun LevelCard(
    level: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        SpotTheme.colors.spotGreen2CD7A6,
                        SpotTheme.colors.spotGreen5DD281
                    )
                ),
                shape = RoundedCornerShape(2.dp)
            )
            .padding(horizontal = 3.dp, vertical = 1.5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.viewfinder_level_format, level),
            style = SpotTheme.typography.label13,
            color = SpotTheme.colors.backgroundWhite,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun LevelCardPreview() {
    LevelCard(
        level = 1
    )
}

