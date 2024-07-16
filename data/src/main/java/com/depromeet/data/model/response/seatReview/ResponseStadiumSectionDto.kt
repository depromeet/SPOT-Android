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
        @SerialName("alias") val alias: String,
        @SerialName("color") val color: Color,
    ) {
        fun toSelectionList(): StadiumSectionModel.SectionListDto {
            return StadiumSectionModel.SectionListDto(
                id = id,
                name = name,
                alias = alias,
                color = color.toColor(),
            )
        }
    }

    @Serializable
    data class Color(
        @SerialName("red") val red: Int,
        @SerialName("green") val green: Int,
        @SerialName("blue") val blue: Int,
    ) {
        fun toColor(): StadiumSectionModel.Color {
            return StadiumSectionModel.Color(
                red = red,
                green = green,
                blue = blue,
            )
        }
    }

    fun toStadiumSection(): StadiumSectionModel {
        return StadiumSectionModel(seatChart, sectionList.map { it.toSelectionList() })
    }
}
