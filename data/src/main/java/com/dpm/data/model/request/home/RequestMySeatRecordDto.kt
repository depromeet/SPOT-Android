package com.dpm.data.model.request.home

import com.dpm.domain.entity.request.home.RequestMySeatRecord
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestMySeatRecordDto(
    @SerialName("page")
    val page: Int? = null,
    @SerialName("size")
    val size: Int? = null,
    @SerialName("year")
    val year: Int? = null,
    @SerialName("month")
    val month: Int? = null,
) {
    companion object {
        fun RequestMySeatRecord.toMySeatRecordRequestDto() = RequestMySeatRecordDto(
            page = page,
            size = size,
            year = year,
            month = month
        )
    }
}
