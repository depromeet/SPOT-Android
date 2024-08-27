package com.dpm.domain.entity.request.home

import com.dpm.domain.model.seatrecord.RecordReviewType

data class RequestMySeatRecord(
    val cursor: String? = null,
    val sortBy: String,
    val size: Int,
    val year: Int? = null,
    val month: Int? = null,
    val reviewType: RecordReviewType? = null,
)


