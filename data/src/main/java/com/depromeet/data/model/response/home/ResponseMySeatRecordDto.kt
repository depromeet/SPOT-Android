package com.depromeet.data.model.response.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseMySeatRecordDto(
    @SerialName("reviews")
    val reviews: List<ResponseReviewDto>,
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
    val filter: ResponseFilterDto,
) {
    @Serializable
    data class ResponseReviewDto(
        @SerialName("id")
        val id: Int,
        @SerialName("stadiumId")
        val stadiumId: Int,
        @SerialName("stadiumName")
        val stadiumName: String,
        @SerialName("blockId")
        val blockId: Int,
        @SerialName("blockName")
        val blockName: String,
        @SerialName("seatId")
        val seatId: Int,
        @SerialName("rowId")
        val rowId: Int,
        @SerialName("seatNumber")
        val seatNumber: Int,
        @SerialName("date")
        val date: String,
        @SerialName("content")
        val content: String,
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("updatedAt")
        val updatedAt: String,
        @SerialName("images")
        val images: List<ResponseReviewImageDto>,
        @SerialName("keywords")
        val keywords: List<ResponseReviewKeywordDto>,
    ) {
        @Serializable
        data class ResponseReviewImageDto(
            @SerialName("id")
            val id: Int,
            @SerialName("url")
            val url: String,
        )

        @Serializable
        data class ResponseReviewKeywordDto(
            @SerialName("id")
            val id: Int,
            @SerialName("content")
            val content: String,
            @SerialName("isPositive")
            val isPositive: Boolean,
        )
    }

    @Serializable
    data class ResponseFilterDto(
        @SerialName("stadiumId")
        val stadiumId: Int,
        @SerialName("year")
        val year: Int,
        @SerialName("month")
        val month: Int,
    )
}
