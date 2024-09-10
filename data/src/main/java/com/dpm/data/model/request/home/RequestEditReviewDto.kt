package com.dpm.data.model.request.home

import com.dpm.domain.entity.request.home.RequestEditReview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestEditReviewDto(
    @SerialName("stadiumId")
    val stadiumId : Int,
    @SerialName("blockId")
    val blockId : Int,
    @SerialName("rowNumber")
    val rowNumber : Int?,
    @SerialName("seatNumber")
    val seatNumber : Int?,
    @SerialName("images")
    val images : List<String>,
    @SerialName("dateTime")
    val dateTime : String,
    @SerialName("good")
    val good : List<String>,
    @SerialName("bad")
    val bad : List<String>,
    @SerialName("content")
    val content : String?,
    @SerialName("reviewType")
    val reviewType : String
)

fun RequestEditReview.toRequestEditReviewDto() = RequestEditReviewDto(
    stadiumId = stadiumId,
    blockId = blockId,
    rowNumber = rowNumber,
    seatNumber = seatNumber,
    images = images,
    dateTime = dateTime,
    good = good,
    bad = bad,
    content = content,
    reviewType = reviewType
)