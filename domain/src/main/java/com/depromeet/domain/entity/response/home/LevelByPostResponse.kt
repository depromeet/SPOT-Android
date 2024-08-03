package com.depromeet.domain.entity.response.home

data class LevelByPostResponse(
    val level : Int = 0,
    val title : String = "",
    val minimum : Int = 0,
    val maximum : Int ?= null
)