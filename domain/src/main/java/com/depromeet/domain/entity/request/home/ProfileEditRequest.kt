package com.depromeet.domain.entity.request.home

data class ProfileEditRequest(
    val url : String ?= null,
    val nickname : String ?= null,
    val teamId : Int ?= null,
)
