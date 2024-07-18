package com.depromeet.domain.entity.request

data class SeatReviewModel(
    val stadiumId: Int,
    val blockId: Int,
    val rowId: Int,
    val seatNumber: Int,
    val images: List<String>,
    val date: String,
    val good: List<String>,
    val bad: List<String>,
    val content: String,
)
