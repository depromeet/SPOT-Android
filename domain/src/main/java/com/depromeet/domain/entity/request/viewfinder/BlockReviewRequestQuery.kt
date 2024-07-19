package com.depromeet.domain.entity.request.viewfinder

data class BlockReviewRequestQuery(
    val rowNumber: Int? = null,
    val seatNumber: Int? = null,
    val offset: Int? = null,
    val limit: Int? = null
)
