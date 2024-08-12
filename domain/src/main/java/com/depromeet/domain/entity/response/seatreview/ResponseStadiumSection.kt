package com.depromeet.domain.entity.response.seatReview

data class ResponseStadiumSection(
    val seatChart: String,
    val sectionList: List<SectionListDto>,
) {
    data class SectionListDto(
        val id: Int,
        val name: String,
        val alias: String?,
        val color: String,
    )
}
