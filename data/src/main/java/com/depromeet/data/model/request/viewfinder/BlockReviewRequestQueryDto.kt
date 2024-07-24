package com.depromeet.data.model.request.viewfinder

data class BlockReviewRequestQueryDto(
    val rowNumber: Int? = null,
    val seatNumber: Int? = null,
    val year: Int? = null,
    val month: Int? = null,
    val page: Int = 0,
    val size: Int = 20
)
