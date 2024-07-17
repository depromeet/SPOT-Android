package com.depromeet.domain.entity.response.viewfinder

data class StadiumResponse(
    val id: Int,
    val name: String = "",
    val homeTeams: List<StadiumsResponse.HomeTeamsResponse> = emptyList(),
    val thumbnail: String = "",
    val stadiumUrl: String = ""
)
