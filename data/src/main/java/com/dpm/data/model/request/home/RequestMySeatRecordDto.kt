package com.dpm.data.model.request.home

import com.dpm.domain.entity.request.home.RequestMySeatRecord
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestMySeatRecordDto(
    @SerialName("cursor")
    val cursor : String ?= null,
    @SerialName("sortBy")
    val sortBy : String,
    @SerialName("size")
    val size: Int,
    @SerialName("year")
    val year: Int? = null,
    @SerialName("month")
    val month: Int? = null,
    @SerialName("reviewType")
    val reviewType : String ?= null
) {
    companion object {
        fun RequestMySeatRecord.toMySeatRecordRequestDto() = RequestMySeatRecordDto(
            cursor = cursor,
            sortBy = sortBy,
            size = size,
            year = year,
            month = month,
            reviewType = reviewType?.name,
        )
    }
}

