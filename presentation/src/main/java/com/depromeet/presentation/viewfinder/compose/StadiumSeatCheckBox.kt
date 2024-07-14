package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import com.depromeet.presentation.viewfinder.sample.Seat

@Composable
fun StadiumSeatCheckBox(
    seat: Seat,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = if (seat.selected) {
                    Color(0xFF121212)
                } else {
                    Color(0xFFFFFFFF)
                },
                shape = RoundedCornerShape(100.dp)
            )
            .border(
                1.dp,
                color = if (seat.selected) {
                    Color(0xFF121212)
                } else {
                    Color(0xFFE5E5E5)
                }, shape = RoundedCornerShape(100.dp)
            )
            .padding(vertical = 10.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (seat.selected) {
                "${seat.column}열 ${seat.number}번"
            } else {
                "좌석 시야"
            },
            fontSize = 13.sp,
            color = if (seat.selected) {
                Color(0xFFFFFFFF)
            } else {
                Color(0xFF121212)
            }
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_down),
            contentDescription = null,
            tint = Color(0xFF878787)
        )
    }
}

@Preview
@Composable
private fun StadiumSeatCheckBoxPreview() {
    StadiumSeatCheckBox(
        seat = Seat(100, 11, false),
        modifier = Modifier

    )
}

@Preview
@Composable
private fun StadiumSeatCheckBoxPreviewSelected() {
    StadiumSeatCheckBox(
        seat = Seat(100, 11, true),
        modifier = Modifier
    )
}