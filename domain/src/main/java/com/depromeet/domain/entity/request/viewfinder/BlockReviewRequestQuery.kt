package com.depromeet.domain.entity.request.viewfinder

data class BlockReviewRequestQuery(
    val rowNumber: Int?,
    val seatNumber: Int?,
    val year: Int?,
    val month: Int?,
    val page: Int,
    val size: Int
) {
    fun rowNumberIsEmpty(): Boolean {
        return rowNumber == null
    }

    fun seatNumberIsEmpty(): Boolean {
        return seatNumber == null
    }
}
