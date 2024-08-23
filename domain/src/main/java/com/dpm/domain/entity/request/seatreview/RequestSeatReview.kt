package com.dpm.domain.entity.request.seatreview

data class RequestSeatReview(
    val rowNumber: Int?,
    val seatNumber: Int?,
    val images: List<String>,
    val good: List<String>,
    val bad: List<String>,
    val content: String?,
    val dateTime: String,
)
