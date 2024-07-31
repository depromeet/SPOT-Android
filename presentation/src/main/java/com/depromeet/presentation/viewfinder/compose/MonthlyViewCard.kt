package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.presentation.R

@Composable
fun MonthlyViewCard(
    month: Int?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    val borderModifier = if (month != null) {
        modifier.border(
            width = 1.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    SpotTheme.colors.spotGreen2CD7A6,
                    SpotTheme.colors.spotGreen5DD281
                )
            ),
            shape = RoundedCornerShape(999.dp)
        )
    } else {
        modifier.border(
            width = 1.dp,
            color = SpotTheme.colors.strokeTertiary,
            shape = RoundedCornerShape(999.dp)
        )
    }

    Row(
        modifier = borderModifier
            .background(
                color = SpotTheme.colors.backgroundWhite,
                shape = RoundedCornerShape(999.dp)
            )
            .clickable { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (month != null) {
                stringResource(id = R.string.viewfinder_month_format, month)
            } else {
                stringResource(id = R.string.viewfinder_monthly_view)
            },
            style = SpotTheme.typography.label05,
            color = SpotTheme.colors.foregroundBodySubtitle
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = if (month != null) {
                painterResource(id = R.drawable.ic_x_close)
            } else {
                painterResource(id = R.drawable.ic_chevron_down)
            },
            contentDescription = null,
            tint = if (month != null) {
                SpotTheme.colors.foregroundCaption
            } else {
                SpotTheme.colors.foregroundDisabled
            },
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    if (month != null) {
                        onCancel()
                    } else {
                        onClick()
                    }
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MonthlyViewCardPreview() {
    MonthlyViewCard(
        month = 1,
        onClick = {},
        onCancel = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MonthlyViewCardNullPreview() {
    MonthlyViewCard(
        month = null,
        onClick = {},
        onCancel = {}
    )
}