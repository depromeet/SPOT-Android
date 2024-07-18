package com.depromeet.domain.entity.request.home

data class MySeatRecordRequest(
    val offset: Int ?= null,
    val limit: Int ?= 50,
    val year: Int ?= 2024,
    val month: Int ?= null,
)


