package com.depromeet.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestMySeatRecordDto(
    @SerialName("offset")
    val offset: Int? = null,
    @SerialName("limit")
    val limit: Int? = null,
    @SerialName("year")
    val year: Int? = null,
    @SerialName("month")
    val month: Int? = null,
)

