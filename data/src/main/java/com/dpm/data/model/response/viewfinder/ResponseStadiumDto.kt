package com.dpm.data.model.response.viewfinder

import com.dpm.domain.entity.response.viewfinder.ResponseStadium
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseStadiumDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("homeTeams")
    val homeTeams: List<ResponseStadiumsDto.ResponseHomeTeamsDto>,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("seatChartWithLabel")
    val seatChartWithLabel: String,
)

fun ResponseStadiumDto.toStadiumResponse() = ResponseStadium(
    id = id,
    name = name,
    homeTeams = homeTeams.map { it.toHomeTeamsResponse() },
    thumbnail = thumbnail,
    stadiumUrl = seatChartWithLabel
)
