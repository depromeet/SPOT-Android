package com.depromeet.data.model.response.viewfinder

import com.depromeet.domain.entity.response.viewfinder.ResponseStadiums
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseStadiumsDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("homeTeams")
    val homeTeams: List<ResponseHomeTeamsDto>,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("isActive")
    val isActive: Boolean,
) {
    @Serializable
    data class ResponseHomeTeamsDto(
        @SerialName("id")
        val id: Int,
        @SerialName("alias")
        val alias: String,
        @SerialName("backgroundColor")
        val backgroundColor: String,
        @SerialName("fontColor")
        val fontColor: String,
    )
}

fun ResponseStadiumsDto.toStadiumsResponse() = ResponseStadiums(
    id = id,
    name = name,
    homeTeams = homeTeams.map { it.toHomeTeamsResponse() },
    thumbnail = thumbnail,
    isActive = isActive
)

fun ResponseStadiumsDto.ResponseHomeTeamsDto.toHomeTeamsResponse() =
    ResponseStadiums.ResponseHomeTeams(
        id = id,
        alias = alias,
        backgroundColor = backgroundColor,
        fontColor = fontColor
    )

