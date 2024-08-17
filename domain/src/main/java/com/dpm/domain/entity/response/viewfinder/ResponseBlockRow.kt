package com.dpm.domain.entity.response.viewfinder

import com.dpm.domain.model.viewfinder.Seat

data class ResponseBlockRow(
    val id: Int,
    val code: String = "",
    val rowInfo: List<ResponseRowInfo> = emptyList()
) {
    data class ResponseRowInfo(
        val id: Int,
        val number: Int = 0,
        val seatNumList: List<Int> = emptyList()
    )

    fun isCheck(column: Int, number: Int): Seat {
        val isNumberExists = rowInfo.any { it.number == column }
        val isSeatNumExistsInAll = rowInfo.any { it.seatNumList.contains(number) }

        if (!isNumberExists && !isSeatNumExistsInAll) {
            return Seat.COLUMN_NUMBER
        }

        if (isNumberExists && !isSeatNumExistsInAll) {
            return Seat.NUMBER
        }

        if (!isNumberExists && isSeatNumExistsInAll) {
            return Seat.COLUMN
        }

        return Seat.CHECK
    }

    fun checkColumnRange(column: Int): Boolean {
        val max = rowInfo.maxByOrNull { it.number }?.number ?: return false
        val min = rowInfo.minByOrNull { it.number }?.number ?: return false

        if (column in min..max) {
            return true
        }
        return false
    }

    fun checkNumberRange(number: Int): Boolean {
        val isSeatNumExistsInAll = rowInfo.any { it.seatNumList.contains(number) }

        if (!isSeatNumExistsInAll) {
            return false
        }

        return true
    }

    fun checkNumberRangeByColumn(column: Int, number: Int): Boolean {
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