package com.depromeet.domain.entity.response.viewfinder

data class BlockRowResponse(
    val id: Int,
    val code: String = "",
    val rowInfo: List<RowInfoResponse> = emptyList()
) {
    data class RowInfoResponse(
        val id: Int,
        val number: Int = 0,
        val seatNumList: List<Int> = emptyList()
    )

    fun checkColumnRange(column: Int): Boolean {
        val max = rowInfo.maxByOrNull { it.number }?.number ?: return false
        val min = rowInfo.minByOrNull { it.number }?.number ?: return false

        if (column in min..max) {
            return true
        }
        return false
    }

    fun checkNumberRange(column: Int, number: Int): Boolean {
        rowInfo.filter { it.number == column }.getOrNull(0)?.let {
            if (number in it.seatNumList) {
                return true
            }
        } ?: return false

        return false
    }

    fun findRowId(column: Int): Int? {
        rowInfo.filter { it.number == column }.getOrNull(0)?.let {
            return it.id
        } ?: return null
    }
}