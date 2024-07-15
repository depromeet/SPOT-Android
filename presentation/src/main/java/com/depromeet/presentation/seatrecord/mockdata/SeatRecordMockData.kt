package com.depromeet.presentation.seatrecord.mockdata

import android.os.Parcelable
import com.depromeet.presentation.extension.extractMonth
import com.depromeet.presentation.viewfinder.sample.Keyword
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.parcelize.Parcelize

data class MonthData(
    val month: Int = 0,
    var isClicked: Boolean = false,
)

data class RecordDetailMockData(
    val profileDetailData: ProfileDetailData = ProfileDetailData(),
    val reviews: List<ReviewMockData> = emptyList(),
)

data class ProfileDetailData(
    val nickName: String= "",
    val profileImage: String ="",
    val level: Int =0,
    val titleName: String = "",
    val recordCount: Int = 0,
)

@Parcelize
data class ReviewMockData(
    val id: Int =0,
    val date: String ="",
    val image: String ="",
    val stadiumName: String ="",
    val blockName: String ="",
    val keyword: List<Keyword> = emptyList(),
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

fun makeSeatRecordData(): Flow<RecordDetailMockData> = flow {
    emit(
        RecordDetailMockData(
            profileDetailData = ProfileDetailData(
                nickName = "노균욱",
                profileImage = "https://picsum.photos/600/400",
                level = 6,
                titleName = "전설의 직관러",
                recordCount = 37
            ),
            reviews = makeRecordDetailData()
        )
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
    MonthData(0, true),
    MonthData(1, false),
    MonthData(2, false),
    MonthData(3, false),
    MonthData(4, false),
    MonthData(5, false),
    MonthData(6, false),
    MonthData(7, false),
    MonthData(8, false),
    MonthData(9, false),
    MonthData(10, false),
    MonthData(11, false),
    MonthData(12, false),
)