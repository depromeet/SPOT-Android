package com.dpm.data.model.request.seatreview

import com.dpm.domain.entity.request.seatreview.RequestSeatReview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestSeatReviewDto(
    @SerialName("rowNumber")
    val rowNumber: Int?,
    @SerialName("seatNumber")
    val seatNumber: Int?,
    @SerialName("images")
    val images: List<String>,
    @SerialName("good")
    val good: List<String>,
    @SerialName("bad")
    val bad: List<String>,
    @SerialName("content")
    val content: String?,
    @SerialName("dateTime")
    val dateTime: String,
)

fun RequestSeatReview.toSeatReview() = RequestSeatReviewDto(
    rowNumber = rowNumber,
    seatNumber = seatNumber,
    images = images,
    good = good,
    bad = bad,
    content = content,
    dateTime = dateTime,
)
