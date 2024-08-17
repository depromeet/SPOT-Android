package com.dpm.domain.entity.response.home

data class ResponseRecentReview(
    val review: ReviewWrapperResponse,
    val reviewCount: Int = 0,
) {
    data class ReviewWrapperResponse(
        val baseReview: ReviewResponse,
        val stadiumName: String = "",
        val sectionName: String = "",
        val blockCode: String = "",
    )

    data class ReviewResponse(
        val id: Int,
        val member: MemberResponse = MemberResponse(),
        val stadium: StadiumResponse,
        val section: SectionResponse,
        val block: BlockResponse,
        val row: RowResponse,
        val seat: SeatResponse,
        val dateTime: String = "",
        val content: String = "",
        val images: List<ImageResponse> = emptyList(),
        val keywords: List<KeywordResponse> = emptyList(),
    )

    data class MemberResponse(
        val profileImage: String? = "",
        val nickname: String = "",
        val level: Int = 0,
    )

    data class StadiumResponse(
        val id: Int,
        val name: String = "",
    )

    data class SectionResponse(
        val id: Int,
        val name: String = "",
        val alias: String? = "",
    )

    data class BlockResponse(
        val id: Int,
        val code: String = "",
    )

    data class RowResponse(
        val id: Int,
        val number: Int = 0,
    )

    data class SeatResponse(
        val id: Int,
        val seatNumber: Int = 0,
    )

    data class ImageResponse(
        val id: Int,
        val url: String = "",
    )

    data class KeywordResponse(
        val id: Int = 0,
        val content: String = "",
        val isPositive: Boolean = false,
    )

}