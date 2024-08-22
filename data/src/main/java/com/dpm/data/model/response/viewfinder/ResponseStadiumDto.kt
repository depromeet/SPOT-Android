package com.dpm.data.model.response.viewfinder

import com.dpm.data.model.response.seatreview.ResponseStadiumSectionDto
import com.dpm.domain.entity.response.viewfinder.ResponseStadium
import com.dpm.domain.entity.response.viewfinder.Section
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
    @SerialName("sections")
    val sections: List<ResponseStadiumSectionDto.SectionListDto>,
    @SerialName("blockTags")
    val blockTags: List<ResponseBlockTagsDto>
) {
    @Serializable
    data class ResponseBlockTagsDto(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("blockCodes")
        val blockCodes: List<String>,
        @SerialName("description")
        val description: String,
    )
}

fun ResponseStadiumDto.toStadiumResponse() = ResponseStadium(
    id = id,
    name = name,
    homeTeams = homeTeams.map { it.toHomeTeamsResponse() },
    thumbnail = thumbnail,
    stadiumUrl = seatChartWithLabel,
    sections = sections.map { it.toSection() },
    blockTags = blockTags.map { it.toResponseBlockTags() }
)

fun ResponseStadiumDto.ResponseBlockTagsDto.toResponseBlockTags() =
    ResponseStadium.ResponseBlockTags(
        id = id,
        name = name,
        blockCodes = blockCodes,
        description = description.trim()
    )

fun ResponseStadiumSectionDto.SectionListDto.toSection() = Section(
    id = id,
    name = name,
    alias = alias ?: "",
    isActive = false
)