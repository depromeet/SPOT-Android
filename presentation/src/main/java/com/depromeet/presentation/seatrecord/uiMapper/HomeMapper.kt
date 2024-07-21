package com.depromeet.presentation.seatrecord.uiMapper

import android.os.Parcelable
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.viewfinder.sample.Keyword
import kotlinx.parcelize.Parcelize

/**
 *  리팩토링 예정
 */
data class MonthReviewData(
    val month: String,
    val reviews: List<MySeatRecordResponse.ReviewResponse>,
)

data class MonthUiData(
    val month: Int = 0,
    var isClicked: Boolean = false,
)

@Parcelize
data class ReviewUiData(
    val id: Int,
    val stadiumId: Int,
    val stadiumName: String,
    val blockId: Int,
    val blockName: String,
    val seatId: Int,
    val rowId: Int,
    val seatNumber: Int,
    val date: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val images: List<ReviewImageUiData>,
    val keywords: List<ReviewKeywordUiData>,
) : Parcelable {
    @Parcelize
    data class ReviewImageUiData(
        val id: Int,
        val url: String,
    ) : Parcelable

    @Parcelize
    data class ReviewKeywordUiData(
        val id: Int,
        val content: String,
        val isPositive: Boolean,
    ) : Parcelable
}

/**
 * 우선 Ui Mapper로 임시 처리 추후 관희 flowRow 변경 사항에 따라 수정 예정
 */
fun MySeatRecordResponse.ReviewResponse.ReviewKeywordResponse.toUiKeyword() =
    Keyword(
        message = content,
        type = if (isPositive) 1 else 0,
        like = 0
    )

fun ReviewUiData.ReviewKeywordUiData.toUiKeyword() =
    Keyword(
        message = content,
        type = if (isPositive) 1 else 0,
        like = 0
    )


fun MySeatRecordResponse.ReviewResponse.toUiModel(): ReviewUiData {
    return ReviewUiData(
        id = id,
        stadiumId = stadiumId,
        stadiumName = stadiumName,
        blockId = blockId,
        blockName = blockName,
        seatId = seatId,
        rowId = rowId,
        seatNumber = seatNumber,
        date = date,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        images = images.map { it.toUiModel() },
        keywords = keywords.map { it.toUiModel() }
    )
}

fun MySeatRecordResponse.ReviewResponse.ReviewImageResponse.toUiModel(): ReviewUiData.ReviewImageUiData {
    return ReviewUiData.ReviewImageUiData(
        id = id,
        url = url
    )
}

fun MySeatRecordResponse.ReviewResponse.ReviewKeywordResponse.toUiModel(): ReviewUiData.ReviewKeywordUiData {
    return ReviewUiData.ReviewKeywordUiData(
        id = id,
        content = content,
        isPositive = isPositive
    )
}