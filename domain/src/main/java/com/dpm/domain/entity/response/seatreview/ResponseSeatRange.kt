package com.dpm.domain.entity.response.seatreview

data class ResponseSeatRange(
    val id: Int,
    val code: String,
    val rowInfo: List<RowInfo>,
) {
    data class RowInfo(
        val id: Int,
        val number: Int,
        val seatNumList: List<Int>,
    )
}
