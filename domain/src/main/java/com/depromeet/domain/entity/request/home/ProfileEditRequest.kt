package com.depromeet.domain.entity.request.home

data class ProfileEditRequest(
    val url : String = "",
    val nickname : String = "",
    val teamId : Int = 0,
)
