package com.depromeet.domain.entity.request

data class SeatReviewModel(
    val images: List<String>,
    val dateTime: String,
    val good: List<String>,
    val bad: List<String>,
    val content: String,
)
