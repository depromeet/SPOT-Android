package com.depromeet.presentation.home.mockdata

import android.os.Parcelable
import com.depromeet.presentation.home.viewmodel.HomeUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

/**
 * 테스트용
 */
@Parcelize
data class Profile(
    val nickName: String = "",
    val writeCount: Int = 0,
    val level: Int = 0,
    val title: String = "",
    val cheerTeam: String = "",
) : Parcelable

@Parcelize
data class RecentSight(
    val location: String = "",
    val date: String = "",
    val section: String = "",
    val photoList: List<String> = emptyList(),
) : Parcelable

data class LevelPolicy(
    val name: String,
    val minCount: Int,
    val maxCount: Int,
    val level: Int,
)

/***
 * 화면 테스트용으로 랜덤하게 flow return
 */
fun mockDataProfile(): Flow<HomeUiState> = flow {

    /***
     * 추후 클라에서 처리할수도 있을지도..? 아닌가
     */
    val levelPolicies = listOf<LevelPolicy>(
        LevelPolicy("직관 첫 걸음", 0, 2, 1),
        LevelPolicy("경기장 탐험가", 3, 6, 2),
        LevelPolicy("직관의 여유", 7, 11, 3),
        LevelPolicy("응원 단장", 12, 20, 4),
        LevelPolicy("야구장 VIP", 21, 35, 5),
        LevelPolicy("전설의 직관러", 36, Int.MAX_VALUE, 6)
    )

    fun getLevelByCount(count: Int): Pair<Int, String> {
        val levelPolicy = levelPolicies.first { count in it.minCount..it.maxCount }
        return Pair(levelPolicy.level, levelPolicy.name)
    }


    val profileList = listOf(
        Profile("노균욱", 23, 5, "야구장 VIP", "https://picsum.photos/600/400"),
        Profile("윤성식", 12, 4, "응원 단장", "https://picsum.photos/600/400"),
        Profile("박민주", 2, 2, "직관 첫 걸음", "https://picsum.photos/600/400"),
        Profile("조관희", 7, 3, "직관의 여유", "https://picsum.photos/600/400"),
    )
    val recentSightList = listOf<RecentSight>(
        RecentSight("서울 잠실 야구장", "2024년 7월 20일", "3루 네이비석 3054블럭", emptyList()),
        RecentSight(
            "대전 한화 야구장",
            "2024년 7월 12일",
            "1루 블루석 124블럭",
            listOf("https://picsum.photos/600/400")
        ),
        RecentSight(
            "고척 스카이돔", "2024년 7월 05일", "3루 레드석 234블럭", listOf(
                "https://picsum.photos/600/400",
                "https://picsum.photos/600/400"
            )
        ),
        RecentSight(
            "대구 삼성 파크", "2024년 6월 23일", "3루 베이지석 3451블럭", listOf(
                "https://picsum.photos/600/400",
                "https://picsum.photos/600/400",
                "https://picsum.photos/600/400"
            )
        )
    )
    delay(1000)
    val randomNumber = Random.nextInt(0, 4)
    emit(
        HomeUiState(
            profileList[randomNumber],
            recentSightList[randomNumber]
        )
    )
}


