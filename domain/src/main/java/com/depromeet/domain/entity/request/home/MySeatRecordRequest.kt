package com.depromeet.domain.entity.request.home

data class MySeatRecordRequest(
    val page: Int ?= null,
    val size: Int ?= 10,
    val year: Int ?= 2024,
    val month: Int ?= null,
)


