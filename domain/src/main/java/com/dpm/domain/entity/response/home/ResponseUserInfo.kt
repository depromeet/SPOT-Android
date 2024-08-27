package com.dpm.domain.entity.response.home

data class ResponseUserInfo(
    val userId: Int = 0,
    val profileImage: String = "",
    val level: Int = 0,
    val levelTitle: String = "",
    val nickname: String = "",
    val reviewCount: Int = 0,
    val totalLikes : Int = 0,
    val teamId: Int? = null,
    val teamName: String? = "",
)