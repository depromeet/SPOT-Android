package com.dpm.domain.entity.response.home

data class ResponseScrap(
    val reviews: List<ResponseReviewWrapper> = emptyList(),
    val nextCursor: String? = null,
    val hasNext: Boolean = false,
    val totalScrapCount: Int = 0,
    val filter: ResponseFilter,
) {
    data class ResponseReviewWrapper(
        val baseReview: ResponseBaseReview,
        val stadiumName: String = "",
        val sectionName: String = "",
        val blockCode: String = "",
    )

    data class ResponseBaseReview(
        val id: Int,
        val member: ResponseMember = ResponseMember(),
        val stadium: ResponseStadium = ResponseStadium(),
        val section: ResponseSection = ResponseSection(),
        val block: ResponseBlock = ResponseBlock(),
        val row: ResponseRow = ResponseRow(),
        val seat: ResponseSeat? = null,
        val dateTime: String = "",
        val content: String = "",
        val images: List<ResponseImage> = emptyList(),
        val keywords: List<ResponseKeyword> = emptyList(),
        val likesCount: Int = 0,
        val scrapsCount: Int = 0,
        val reviewType: String = "",
    )

    data class ResponseMember(
        val profileImage: String? = null,
        val nickname: String = "",
        val level: Int = 0,
    )

    data class ResponseStadium(
        val id: Int = 0,
        val name: String = "",
    )

    data class ResponseSection(
        val id: Int = 0,
        val name: String = "",
        val alias: String? = null,
    )

    data class ResponseBlock(
        val id: Int = 0,
        val code: String = "",
    )

    data class ResponseRow(
        val id: Int = 0,
        val number: Int = 0,
    )

    data class ResponseSeat(
        val id: Int = 0,
        val seatNumber: Int = 0,
    )

    data class ResponseImage(
        val id: Int = 0,
        val url: String = "",
    )

    data class ResponseKeyword(
        val id: Int = 0,
        val content: String = "",
        val isPositive: Boolean = false,
    )

    data class ResponseFilter(
        val stadiumId: Int = 0,
        val months: List<Int> = emptyList(),
        val good: List<String> = emptyList(),
        val bad: List<String> = emptyList(),
    )
}