package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.RecentReviewResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecentReviewDto(
    @SerialName("totalReviewCount")
    val totalReviewCount: Int,
    @SerialName("stadiumName")
    val stadiumName: String,
    @SerialName("date")
    val date: String,
    @SerialName("reviewImages")
    val reviewImages: List<String>,
    @SerialName("blockName")
    val blockName: String,
    @SerialName("seatNumber")
    val seatNumber: Long,
) {
    companion object {
        fun ResponseRecentReviewDto.toRecentReviewResponse() = RecentReviewResponse(
            totalReviewCount = totalReviewCount,
            stadiumName = stadiumName,
            date = date,
            reviewImages = reviewImages,
            blockName = blockName,
            seatNumber = seatNumber
        )
    }
}
