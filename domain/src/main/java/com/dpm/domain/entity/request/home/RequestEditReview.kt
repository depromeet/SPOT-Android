package com.dpm.domain.entity.request.home

data class RequestEditReview(
    val stadiumId : Int,
    val blockId : Int,
    val rowNumber : Int?,
    val seatNumber : Int?,
    val images : List<String>,
    val dateTime : String,
    val good : List<String>,
    val bad : List<String>,
    val content : String?,
    val reviewType : String,
)