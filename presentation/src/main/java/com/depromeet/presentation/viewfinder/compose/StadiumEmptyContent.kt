package com.depromeet.presentation.viewfinder.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_review),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "$blockNumber 블록에 등록된\n시야 후기가 없습니다.",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "다른 블록에서 새로운 시야를 찾아보세요!",
            fontSize = 15.sp,
            color = Color(0xFF606060),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onGoBack,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(width = 1.dp, color = Color(0xFFE5E5E5)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFFFFF)),
        ) {
            Text(
                text = "돌아가기",
                color = Color(0xFF121212),
                fontSize = 14.sp
            )
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
