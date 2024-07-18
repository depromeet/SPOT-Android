package com.depromeet.presentation.seatrecord.uiMapper

import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.viewfinder.sample.Keyword

/**
 *  리팩토링 예정
 */
data class MonthReviewData(
    val month: String,
    val reviews: List<MySeatRecordResponse.ReviewResponse>,
)

/**
 * 우선 Ui Mapper로 임시 처리 추후 관희 flowRow 변경 사항에 따라 수정 예정
 */
fun MySeatRecordResponse.ReviewResponse.ReviewKeywordResponse.toUiKeyword() =
    Keyword(
        message = content,
        type = if (isPositive) 1 else 0,
        like = 0
    )