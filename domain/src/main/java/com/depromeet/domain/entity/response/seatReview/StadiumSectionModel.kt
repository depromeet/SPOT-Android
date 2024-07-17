package com.depromeet.domain.entity.response.seatReview

data class StadiumSectionModel(
    val seatChart: String,
    val sectionList: List<SectionListDto>,
) {
    data class SectionListDto(
        val id: Int,
        val name: String,
        val alias: String = "",
        val color: Color,
    )

    data class Color(
        val red: Int,
        val green: Int,
        val blue: Int,
    )
}
