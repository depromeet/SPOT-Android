package com.depromeet.data.model.response.seatReview

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
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("alias")
        val alias: String,
        @SerialName("color")
        val color: Color,
    )

    @Serializable
    data class Color(
        @SerialName("red")
        val red: Int,
        @SerialName("green")
        val green: Int,
        @SerialName("blue")
        val blue: Int,
    )
}
