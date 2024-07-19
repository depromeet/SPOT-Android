package com.depromeet.data.model.response.viewfinder

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockReviewResponseDto(
    @SerialName("stadiumTitle")
    val stadiumTitle: String,
    @SerialName("seatContent")
    val seatContent: String,
    @SerialName("header")
    val header: List<HeaderResponseDto>,
    @SerialName("keywords")
    val keywords: List<KeywordResponseDto>,
    @SerialName("reviews")
    val reviews: List<ReviewResponseDto>,
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("filteredCount")
    val filteredCount: Int,
    @SerialName("offset")
    val offset: Int,
    @SerialName("limit")
    val limit: Int,
    @SerialName("hasMore")
    val hasMore: Boolean,
    @SerialName("filter")
    val filter: ReviewFilterResponseDto
) {
    @Serializable
    data class HeaderResponseDto(
        @SerialName("url")
        val url: String,
        @SerialName("content")
        val content: String
    )

    @Serializable
    data class KeywordResponseDto(
        @SerialName("content")
        val content: String,
        @SerialName("count")
        val count: Int,
        @SerialName("isPositive")
        val isPositive: Boolean
    )

    @Serializable
    data class ReviewResponseDto(
        @SerialName("id")
        val id: Long,
        @SerialName("userId")
        val userId: Long,
        @SerialName("blockId")
        val blockId: Int,
        @SerialName("seatId")
        val seatId: Int,
        @SerialName("rowId")
        val rowId: Int,
        @SerialName("seatNumber")
        val seatNumber: Int,
        @SerialName("date")
        val date: String,
        @SerialName("seatContent")
        val seatContent: String,
        @SerialName("content")
        val content: String,
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("updatedAt")
        val updatedAt: String,
        @SerialName("images")
        val images: List<ReviewImageResponseDto>,
        @SerialName("keywords")
        val keywords: List<ReviewKeywordResponseDto>,

        ) {
        @Serializable
        data class ReviewImageResponseDto(
            @SerialName("id")
            val id: Int,
            @SerialName("url")
            val url: String,
        )

        @Serializable
        data class ReviewKeywordResponseDto(
            @SerialName("id")
            val id: Int,
            @SerialName("content")
            val content: String,
            @SerialName("isPositive")
            val isPositive: Boolean,
        )
    }

    @Serializable
    data class ReviewFilterResponseDto(
        @SerialName("rowId")
        val rowId: Int,
        @SerialName("seatNumber")
        val seatNumber: Int,
    )
}
