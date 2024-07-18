package com.depromeet.domain.entity.response.home

data class MySeatRecordResponse(
    val reviews: List<ReviewResponse> = emptyList(),
    val totalCount: Int = 0,
    val filteredCount: Int = 0,
    val offset: Int = 0,
    val limit: Int = 0,
    val hasMore: Boolean = false,
    val filter: FilterResponse = FilterResponse(),
) {
    data class ReviewResponse(
        val id: Int,
        val stadiumId: Int,
        val stadiumName: String = "",
        val blockId: Int = 0,
        val blockName: String = "",
        val seatId: Int = 0,
        val rowId: Int = 0,
        val seatNumber: Int = 0,
        val date: String = "",
        val content: String = "",
        val createdAt: String = "",
        val updatedAt: String = "",
        val images: List<ReviewImageResponse> = emptyList(),
        val keywords: List<ReviewKeywordResponse> = emptyList(),
    ) {
        data class ReviewImageResponse(
            val id: Int = 0,
            val url: String = "",
        )

        data class ReviewKeywordResponse(
            val id: Int = 0,
            val content: String = "",
            val isPositive: Boolean = false,
        )
    }

    data class FilterResponse(
        val stadiumId: Int = 0,
        val year: Int = 0,
        val month: Int = 0,
    )
}
