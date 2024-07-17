package com.depromeet.data.model.response.viewfinder

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StadiumResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("homeTeams")
    val homeTeams: List<StadiumsResponseDto.HomeTeamsResponseDto>,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("seatChartWithLabel")
    val seatChartWithLabel: String,
)
