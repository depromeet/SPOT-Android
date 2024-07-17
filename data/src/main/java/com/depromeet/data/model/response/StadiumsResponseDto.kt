package com.depromeet.data.model.response

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
        val color: ColorResponseDto,
    ) {
        @Serializable
        data class ColorResponseDto(
            @SerialName("red")
            val red: Int,
            @SerialName("green")
            val green: Int,
            @SerialName("blue")
            val blue: Int,
        )
    }
}


