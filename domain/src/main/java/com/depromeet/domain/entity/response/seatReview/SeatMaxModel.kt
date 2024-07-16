package com.depromeet.domain.entity.response.seatReview

data class SeatMaxModel(
    val id: Int,
    val code: String,
    val rowInfo: List<RowInfo>,
) {
    data class RowInfo(
        val id: Int,
        val number: Int,
        val minSeatNum: Int,
        val maxSeatNum: Int,
    )
}
