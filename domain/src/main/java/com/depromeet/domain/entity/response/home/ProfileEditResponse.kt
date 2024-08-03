package com.depromeet.domain.entity.response.home

data class ProfileEditResponse(
    val id: Int,
    val nickname: String = "",
    val teamId: Int?,
)