package com.depromeet.data.model.response.seatReview

import com.depromeet.domain.entity.response.seatReview.StadiumSectionModel
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
        fun toSelectionList(): StadiumSectionModel.SectionListDto {
            return StadiumSectionModel.SectionListDto(
                id = id,
                name = name,
                alias = alias,
                color = color
            )
        }
    }

    fun toStadiumSection(): StadiumSectionModel {
        return StadiumSectionModel(seatChart, sectionList.map { it.toSelectionList() })
    }
}
