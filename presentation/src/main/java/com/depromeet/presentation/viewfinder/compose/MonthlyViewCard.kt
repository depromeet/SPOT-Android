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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depromeet.presentation.R

@Composable
fun MonthlyViewCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(color = Color.White, shape = RoundedCornerShape(100.dp))
            .border(width = 1.dp, color = Color(0xFFE5E5E5), shape = RoundedCornerShape(100.dp))
            .clickable { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "월별 시야",
            fontSize = 13.sp,
            color = Color(0xFF121212)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_down),
            contentDescription = null,
            tint = Color(0xFF9F9F9F),
            modifier = Modifier.size(12.dp)
        )
    }
}

@Preview
@Composable
private fun MonthlyViewCardPreview() {
    MonthlyViewCard(
        onClick = {}
    )
}