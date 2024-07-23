package com.depromeet.domain.entity.response.home

data class MySeatRecordResponse(
    val reviews: List<ReviewResponse> = emptyList(),
    val totalElements : Int = 0,
    val totalPages : Int = 0,
    val number : Int = 0,
    val size : Int = 0,
) {
    data class ReviewResponse(
        val id: Int,
        val stadiumId: Int,
        val stadiumName: String = "",
        val blockId: Int = 0,
        val blockName: String = "",
        val seatId: Int = 0,
        val rowId: Int = 0,
        val rowNumber : Int = 0,
        val seatNumber: Int = 0,
        val date: String = "",
        val content: String = "",
        val member : MemberResponse = MemberResponse(),
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

        data class MemberResponse(
            val profileImage : String = "",
            val nickname : String = "",
            val level : Int = 0
        )
    }

}
