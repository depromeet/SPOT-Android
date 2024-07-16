package com.depromeet.data.model.request

import com.depromeet.domain.entity.request.SeatReviewModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestSeatReviewDto(
    @SerialName("stadiumId")
    val stadiumId: Int,
    @SerialName("blockId")
    val blockId: Int,
    @SerialName("rowId")
    val rowId: Int,
    @SerialName("seatNumber")
    val seatNumber: Int,
    @SerialName("images")
    val images: List<String>,
    @SerialName("date")
    val date: String,
    @SerialName("good")
    val good: List<String>,
    @SerialName("bad")
    val bad: List<String>,
    @SerialName("content")
    val content: String,
)

fun SeatReviewModel.toSeatReview() = RequestSeatReviewDto(
    stadiumId = stadiumId,
    blockId = blockId,
    rowId = rowId,
    seatNumber = seatNumber,
    images = images,
    date = date,
    good = good,
    bad = bad,
    content = content,
)
