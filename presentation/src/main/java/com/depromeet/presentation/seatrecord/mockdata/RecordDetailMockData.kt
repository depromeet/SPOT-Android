package com.depromeet.presentation.seatrecord.mockdata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ReviewDetailMockData(
    val reviewId: Int,
    val profileImage: String,
    val nickName: String,
    val level: Int,
    val stadiumName: String,
    val blockName: String,
    val createdAt: String,
    val images: List<String>,
    val content: String,
    val keywords: List<String>,
) : Parcelable

fun makeReviewDetailListData(): List<ReviewDetailMockData> {
    val list = mutableListOf<ReviewDetailMockData>()

    for (i in 1..8) {
        list.add(
            ReviewDetailMockData(
                reviewId = 1,
                profileImage = "https://picsum.photos/600/400",
                nickName = "노균욱",
                level = 6,
                stadiumName = "서울 잠실 야구장",
                blockName = "1루 네이비석 304블록 3열 12번",
                createdAt = "2024-07-12",
                images = listOf<String>(
                    "https://picsum.photos/600/400",
                    "https://picsum.photos/600/400",
                    "https://picsum.photos/600/400"
                ),
                content = "제일 앞열이라 시야는 아주 좋았어요. 다만, 앞쪽으로 사람들이 많이 다니면 조금 신경 쓰일 수 있어요.",
                keywords = listOf<String>(
                    "\uD83D\uDE4D\u200D서서 응원하는 존",
                    "☀️ 온종일 햇빛 존",
                    "\uD83D\uDE4D\u200D서서 응원하는 존",
                    "☀️ 온종일 햇빛 존",
                    "\uD83D\uDE4D\u200D서서 응원하는 존"
                )
            )
        )
    }
    return list
}