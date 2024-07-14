package com.depromeet.presentation.seatrecord.mockdata

import android.os.Parcelable
import com.depromeet.presentation.extension.extractMonth
import com.depromeet.presentation.viewfinder.sample.Keyword
import kotlinx.parcelize.Parcelize

data class MonthData(
    val month: String, //int?
    val isClicked: Boolean,
)

data class RecordDetailMockData(
    val profileDetailData: ProfileDetailData,
    val reviews: List<ReviewMockData>,
)

data class ProfileDetailData(
    val nickName: String,
    val profileImage: String,
    val level: Int,
    val titleName: String,
    val recordCount: Int,
)

@Parcelize
data class ReviewMockData(
    val id: Int,
    val date: String,
    val image: String,
    val stadiumName: String,
    val blockName: String,
    val keyword: List<Keyword>,
) : Parcelable

data class MonthReviewData(
    val month: String,
    val reviews: List<ReviewMockData>,
)

fun List<ReviewMockData>.groupByMonth(): List<MonthReviewData> {
    val groupedData = this.groupBy { it.date.extractMonth(false) }
    return groupedData.map { (month, reviews) ->
        MonthReviewData(month, reviews)
    }
}

fun makeSeatRecordData(): RecordDetailMockData {
    return RecordDetailMockData(
        profileDetailData = ProfileDetailData(
            nickName = "노균욱",
            profileImage = "https://picsum.photos/600/400",
            level = 6,
            titleName = "전설의 직관러",
            recordCount = 37
        ),
        reviews = makeRecordDetailData()
    )
}


fun makeRecordDetailData(): List<ReviewMockData> {
    val list = mutableListOf<ReviewMockData>()
    for (i in 1..8) {
        list.add(
            ReviewMockData(
                id = i,
                date = "2024-0${i % 4 + 1}-12",
                image = "https://picsum.photos/600/400",
                stadiumName = "서울 잠실 야구장",
                blockName = "1루 네이비석 304블록",
                keyword = listOf<Keyword>(
                    Keyword("🙍‍서서 응원하는 존", 44, 0),
                    Keyword("☀️ 온종일 햇빛 존", 44, 1),
                    Keyword("🙍‍서서 응원하는 존", 44, 1),
                    Keyword("🙍‍서서 응원하는 존", 44, 0),
                    Keyword("🙍‍서서 응원하는 존", 44, 0)
                ),
            )
        )
    }
    return list
}

val monthList = listOf<MonthData>(
    MonthData("전체", true),
    MonthData("3월", false),
    MonthData("4월", false),
    MonthData("4월", false),
    MonthData("5월", false),
    MonthData("6월", false),
    MonthData("7월", false),
    MonthData("8월", false),
    MonthData("9월", false),
)