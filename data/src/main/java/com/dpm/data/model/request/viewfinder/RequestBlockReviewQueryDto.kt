package com.dpm.data.model.request.viewfinder

import com.dpm.domain.entity.request.viewfinder.RequestBlockReviewQuery

data class RequestBlockReviewQueryDto(
    val rowNumber: Int? = null,
    val seatNumber: Int? = null,
    val year: Int? = null,
    val month: Int? = null,
    val cursor: String? = null,
    val sortBy: String = "DATE_TIME",
    val size: Int = 20
)

fun RequestBlockReviewQuery.toBlockReviewRequestQueryDto() = RequestBlockReviewQueryDto(
    rowNumber = rowNumber,
    seatNumber = seatNumber,
    year = year,
    month = month,
    cursor = cursor,
    sortBy = sortBy,
    size = size
)