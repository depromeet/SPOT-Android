package com.depromeet.domain.entity.response.viewfinder

data class StadiumsResponse(
    val id: Int,
    val name: String = "",
    val homeTeams: List<HomeTeamsResponse> = emptyList(),
    val thumbnail: String = "",
    val isActive: Boolean = false,
) {
    data class HomeTeamsResponse(
        val id: Int,
        val alias: String = "",
        val color: String = "",
    )
}