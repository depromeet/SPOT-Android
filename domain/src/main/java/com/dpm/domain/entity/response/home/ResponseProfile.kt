package com.dpm.domain.entity.response.home

data class ResponseProfile(
    val teamId : Int = 0,
    val profileImage : String = "",
    val nickname :String = "",
    val level : Int = 0,
    val levelTitle : String = "",
    val teamImage : String = ""
)