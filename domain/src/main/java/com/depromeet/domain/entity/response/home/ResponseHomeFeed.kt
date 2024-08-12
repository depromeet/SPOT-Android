package com.depromeet.domain.entity.response.home

data class ResponseHomeFeed(
    val level: Int = 0,
    val teamName: String?,
    val teamId: Int?,
    val levelTitle: String = "",
    val reviewCntToLevelup: Int = 0,
    val mascotImageUrl: String = "",
)