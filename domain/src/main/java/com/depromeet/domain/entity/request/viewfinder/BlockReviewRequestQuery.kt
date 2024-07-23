package com.depromeet.domain.entity.request.viewfinder

data class BlockReviewRequestQuery(
    val rowNumber: Int? = null,
    val seatNumber: Int? = null,
    val year: Int? = null,
    val month: Int? = null,
    val page: Int? = null,
    val size: Int? = null
)
