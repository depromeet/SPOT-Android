package com.dpm.domain.entity.response.viewfinder

data class ResponseStadiums(
    val id: Int,
    val name: String = "",
    val homeTeams: List<ResponseHomeTeams> = emptyList(),
    val thumbnail: String = "",
    val isActive: Boolean = false,
) {
    data class ResponseHomeTeams(
        val id: Int,
        val alias: String = "",
        val fontColor: String = ""
    )
}