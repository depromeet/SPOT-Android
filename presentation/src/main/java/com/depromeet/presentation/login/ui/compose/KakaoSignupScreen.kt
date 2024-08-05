package com.depromeet.presentation.login.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depromeet.designsystem.compose.ui.SpotTheme
import com.depromeet.presentation.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KakaoSignupScreen(

) {
    val onBoardingList = listOf(
        Triple("시야찾기로 원하는 야구장\n" +
                "자리를 빠르게 알아봐요!", 0, 3),
        Triple("내 시야 후기를 올려서\n" +
                "캐릭터를 성장시켜요!", 0, 6),
        Triple("내 소중한 시야 기록을\n" +
                "한 자리에서 봐요!", 6, 10),
    )

    val onboardingImageList = listOf(
        R.drawable.ic_signup_image_1,
        R.drawable.ic_signup_image_3,
        R.drawable.ic_signup_image_2,
    )

    val pageCount = onBoardingList.size
    val pagerState = rememberPagerState(
        pageCount = { pageCount },
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment= Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = onboardingImageList[pagerState.currentPage]),
                    contentDescription = "onboarding",
                    modifier = Modifier
                        .padding(horizontal = 44.dp)
                        .aspectRatio(1f / 2f)
                        .weight(1f)
                )
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
            ) { page ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(top = 44.dp, bottom = 20.dp),
                        text = onBoardingList[page].first,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontWeight = FontWeight(600),
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    if (pagerState.currentPage == iteration) {
                        Box(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    color = SpotTheme.colors.actionEnabled
                                )
                                .size(
                                    height = 6.dp,
                                    width = 15.dp
                                )
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clip(CircleShape)
                                .background(
                                    color = SpotTheme.colors.backgroundPrimary
                                )
                                .size(6.dp)
                        )
                    }
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 30.dp),
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFFEE500)
                ),
                onClick = { /*TODO*/ },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_kakao_logo),
                        contentDescription = "kakao logo",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "카카오 로그인",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 10.dp),
                        fontWeight = FontWeight(600),
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun KakaoSignupScreenPreview() {
    KakaoSignupScreen()
}