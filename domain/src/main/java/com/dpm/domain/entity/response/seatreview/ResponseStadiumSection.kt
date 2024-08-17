package com.dpm.domain.entity.response.seatreview

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
