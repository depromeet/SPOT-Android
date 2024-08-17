package com.dpm.domain.entity.request.home

data class RequestMySeatRecord(
    val page: Int ?= null,
    val size: Int ?= 10,
    val year: Int ?= 2024,
    val month: Int ?= null,
)


