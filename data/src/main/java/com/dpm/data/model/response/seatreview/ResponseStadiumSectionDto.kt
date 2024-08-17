package com.dpm.data.model.response.seatreview

import com.dpm.domain.entity.response.seatreview.ResponseStadiumSection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseStadiumSectionDto(
    @SerialName("seatChart")
    val seatChart: String,
    @SerialName("sectionList")
    val sectionList: List<SectionListDto>,
) {
    @Serializable
    data class SectionListDto(
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String,
        @SerialName("alias") val alias: String?,
        @SerialName("color") val color: String,
    ) {
        fun toSelectionList(): ResponseStadiumSection.SectionListDto {
            return ResponseStadiumSection.SectionListDto(
                id = id,
                name = name,
                alias = alias,
                color = color
            )
        }
    }

    fun toStadiumSection(): ResponseStadiumSection {
        return ResponseStadiumSection(seatChart, sectionList.map { it.toSelectionList() })
    }
}
