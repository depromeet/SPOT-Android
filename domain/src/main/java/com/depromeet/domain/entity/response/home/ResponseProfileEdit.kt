package com.depromeet.domain.entity.response.home

data class ResponseProfileEdit(
    val id: Int,
    val nickname: String = "",
    val teamId: Int?,
)