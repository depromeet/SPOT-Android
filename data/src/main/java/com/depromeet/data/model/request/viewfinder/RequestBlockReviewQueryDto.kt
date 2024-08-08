package com.depromeet.data.model.request.viewfinder

import com.depromeet.domain.entity.request.viewfinder.RequestBlockReviewQuery

data class RequestBlockReviewQueryDto(
    val rowNumber: Int? = null,
    val seatNumber: Int? = null,
    val year: Int? = null,
    val month: Int? = null,
    val page: Int = 0,
    val size: Int = 20
)

fun RequestBlockReviewQuery.toBlockReviewRequestQueryDto() = RequestBlockReviewQueryDto(
    rowNumber = rowNumber,
    seatNumber = seatNumber,
    year = year,
    month = month,
    page = page,
    size = size
)