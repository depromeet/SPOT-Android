package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depromeet.presentation.viewfinder.sample.Stadium
import com.depromeet.presentation.viewfinder.sample.StadiumArea

@Composable
fun StadiumAreaText(
    stadium: String,
    seatContent: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stadium,
            fontSize = 12.sp,
            color = Color(0xFF606060)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = seatContent,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF121212)
        )
    }
}

@Preview
@Composable
private fun StadiumAreaTextPreview() {
    StadiumAreaText(
        stadium = "서울 잠실 야구장",
        seatContent = "오렌지석 207블럭"
    )
}