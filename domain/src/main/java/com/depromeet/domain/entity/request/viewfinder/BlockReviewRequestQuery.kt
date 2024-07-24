package com.depromeet.domain.entity.request.viewfinder

data class BlockReviewRequestQuery(
    val rowNumber: Int? = null,
    val seatNumber: Int? = null,
    val year: Int? = null,
    val month: Int? = null,
    val page: Int = 0,
    val size: Int = 20
) {
    fun rowNumberIsEmpty(): Boolean {
        return rowNumber == null
    }

    fun seatNumberIsEmpty(): Boolean {
        return seatNumber == null
    }
}
