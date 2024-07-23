package com.depromeet.domain.entity.response.home

data class ProfileResponse(
    val profileImage : String = "",
    val nickname :String = "",
    val level : Int = 0,
    val teamImage : String = ""
)