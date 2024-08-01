package com.depromeet.data.model.response.viewfinder

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StadiumsResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("homeTeams")
    val homeTeams: List<HomeTeamsResponseDto>,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("isActive")
    val isActive: Boolean,
) {
    @Serializable
    data class HomeTeamsResponseDto(
        @SerialName("id")
        val id: Int,
        @SerialName("alias")
        val alias: String,
        @SerialName("color")
        val color: String,
    )
}


