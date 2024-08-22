package com.dpm.domain.entity.request.home

data class RequestMySeatRecord(
    val cursor: String? = null,
    val sortBy: String,
    val size: Int,
    val year: Int? = null,
    val month: Int? = null,
)


