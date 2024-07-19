package com.depromeet.domain.entity.response.viewfinder

import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class BlockReviewResponse(
    val stadiumTitle: String = "",
    val seatContent: String = "",
    val header: List<HeaderResponse>,
    val keywords: List<KeywordResponse> = emptyList(),
    val reviews: List<ReviewResponse> = emptyList(),
    val totalCount: Int = 0,
    val filteredCount: Int = 0,
    val offset: Int = 0,
    val limit: Int = 0,
    val hasMore: Boolean = false,
    val filter: ReviewFilterResponse = ReviewFilterResponse()
) {
    data class HeaderResponse(
        val url: String = "",
        val content: String = ""
    )
    data class KeywordResponse(
        val content: String = "",
        val count: Int = 0,
        val isPositive: Boolean = false
    )

    data class ReviewResponse(
        val id: Long,
        val userId: Long,
        val blockId: Int = 0,
        val seatId: Int = 0,
        val rowId: Int = 0,
        val seatNumber: Int = 0,
        val date: String = "",
        val seatContent: String = "",
        val content: String = "",
        val createdAt: String = "",
        val updatedAt: String = "",
        val images: List<ReviewImageResponse> = emptyList(),
        val keywords: List<KeywordResponse> = emptyList(),
    ) {
        data class ReviewImageResponse(
            val id: Int,
            val url: String = "",
        )

        fun formattedDate(): String {
            val localDate = LocalDate.parse(date)

            val formatter = DateTimeFormatter.ofPattern("M월d일")
            val formattedDate = localDate.format(formatter)
            return formattedDate
        }
    }

    data class ReviewFilterResponse(
        val rowId: Int = 0,
        val seatNumber: Int = 0,
    )
}
