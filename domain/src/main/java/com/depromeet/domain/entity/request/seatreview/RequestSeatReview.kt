package com.depromeet.domain.entity.request.seatreview

data class RequestSeatReview(
    val images: List<String>,
    val dateTime: String,
    val good: List<String>,
    val bad: List<String>,
    val content: String?,
)
