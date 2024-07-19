package com.depromeet.data.model.request.viewfinder

data class BlockReviewRequestQueryDto(
    val rowNumber: Int? = null,
    val seatNumber: Int? = null,
    val offset: Int? = null,
    val limit: Int? = null
)
