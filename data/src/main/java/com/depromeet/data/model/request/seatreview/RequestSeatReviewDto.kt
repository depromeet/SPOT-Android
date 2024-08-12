package com.depromeet.data.model.request.seatReview

import com.depromeet.domain.entity.request.seatReview.RequestSeatReview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestSeatReviewDto(
    @SerialName("images")
    val images: List<String>,
    @SerialName("dateTime")
    val dateTime: String,
    @SerialName("good")
    val good: List<String>,
    @SerialName("bad")
    val bad: List<String>,
    @SerialName("content")
    val content: String?,
)

fun RequestSeatReview.toSeatReview() = RequestSeatReviewDto(
    images = images,
    dateTime = dateTime,
    good = good,
    bad = bad,
    content = content,
)
