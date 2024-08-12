package com.depromeet.domain.entity.response.home

data class ResponseReviewDate(
    val yearMonths: List<YearMonths> = emptyList(),
) {
    data class YearMonths(
        val year: Int = 2024,
        val months: List<MonthData> = emptyList(),
        val isClicked: Boolean = false,
    )

    data class MonthData(
        val month: Int = 0,
        val isClicked: Boolean = false,
    )
}
