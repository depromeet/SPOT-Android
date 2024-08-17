package com.dpm.domain.entity.response.home

data class ResponseLevelByPost(
    val level : Int = 0,
    val title : String = "",
    val minimum : Int = 0,
    val maximum : Int ?= null
)