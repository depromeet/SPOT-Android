package com.dpm.domain.entity.request.home

data class RequestProfileEdit(
    val url : String = "",
    val nickname : String = "",
    val teamId : Int = 0,
)
