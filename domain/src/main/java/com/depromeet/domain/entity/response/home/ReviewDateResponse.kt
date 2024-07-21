package com.depromeet.domain.entity.response.home

data class ReviewDateResponse(
    val yearMonths: List<YearMonths> = emptyList(),
) {
    data class YearMonths(
        val year: Int = 2024,
        val months: List<Int> = emptyList(),
    )
}
