package com.dpm.presentation.login.ui.compose

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.depromeet.presentation.R
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.presentation.extension.noRippleClickable
import com.dpm.presentation.util.HighlightedText
import com.dpm.presentation.util.MultiStyleText
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KakaoSignupScreen(
    onKakaoLoginClick: () -> Unit = {}
) {
    val onBoardingList = listOf(
        Pair("시야찾기로 원하는 야구장\n자리를 빠르게 알아봐요!", listOf(0..3)),
        Pair("내 시야 후기를 올려서\n캐릭터를 성장시켜요!", listOf(0..6)),
        Pair("내 소중한 시야 기록을\n한 자리에서 봐요!", listOf(6..10)),
        Pair("내 직관 후기를 기록하면서\n좌석 시야도 공유할 수 있어요", listOf(2..6, 14..19))
    )

    val onboardingImageList = listOf(
        R.drawable.ic_signup_image_1,
        R.drawable.ic_signup_image_2,
        R.drawable.ic_signup_image_3,
        R.drawable.ic_signup_image_4
    )

    val pageCount = onBoardingList.size
    val pagerState = rememberPagerState(
        pageCount = { pageCount },
    )

    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % pageCount)
        }
    }

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
                    contentAlignment = Alignment.Center,
                ) {
                    HighlightedText(
                        text = onBoardingList[page].first,
                        style = SpotTheme.typography.title04,
                        highlightRanges = onBoardingList[page].second,
                        modifier = Modifier.padding(top = 44.dp, bottom = 20.dp)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp, horizontal = 40.dp)
                    .noRippleClickable {
                        onKakaoLoginClick()
                    }
                    .background(Color(0xFFFEE500), RoundedCornerShape(40.dp))
                    .padding(horizontal = 40.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_kakao_logo),
                    contentDescription = "kakao logo",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "카카오 로그인",
                    style = SpotTheme.typography.subtitle02
                )
            }
        }
    }
}

@Composable
@Preview
fun KakaoSignupScreenPreview() {
    KakaoSignupScreen()
}