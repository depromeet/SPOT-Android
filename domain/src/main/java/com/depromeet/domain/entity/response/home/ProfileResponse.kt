package com.depromeet.domain.entity.response.home

data class ProfileResponse(
    val teamId : Int = 0, // TODO : 서버에서 내려주는거 확인하고 투두 지우기
    val profileImage : String = "",
    val nickname :String = "",
    val level : Int = 0,
    val teamImage : String = ""
)