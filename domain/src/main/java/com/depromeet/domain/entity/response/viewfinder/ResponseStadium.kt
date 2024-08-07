package com.depromeet.domain.entity.response.viewfinder

data class ResponseStadium(
    val id: Int,
    val name: String = "",
    val homeTeams: List<ResponseStadiums.ResponseHomeTeams> = emptyList(),
    val thumbnail: String = "",
    val stadiumUrl: String = ""
)
