package com.depromeet.data.model.response.viewfinder

import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockReviewResponseDto(
    @SerialName("keywords")
    val keywords: List<KeywordResponseDto>,
    @SerialName("reviews")
    val reviews: List<ReviewResponseDto>,
    @SerialName("topReviewImages")
    val topReviewImages: List<TopReviewImagesResponseDto>,
    @SerialName("totalElements")
    val totalElements: Long,
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("number")
    val number: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("first")
    val first: Boolean,
    @SerialName("last")
    val last: Boolean,
    @SerialName("filter")
    val filter: ReviewFilterResponseDto
) {
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
        @SerialName("member")
        val member: ReviewMemberResponseDto,
        @SerialName("stadium")
        val stadium: ReviewStadiumResponseDto,
        @SerialName("section")
        val section: ReviewSectionResponseDto,
        @SerialName("block")
        val block: ReviewBlockResponseDto,
        @SerialName("row")
        val row: ReviewRowResponseDto,
        @SerialName("seat")
        val seat: ReviewSeatResponseDto,
        @SerialName("seatNumber")
        val seatNumber: Int,
        @SerialName("dateTime")
        val dateTime: String,
        @SerialName("content")
        val content: String,
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
        ) {
            fun toReviewImageResponse() = BlockReviewResponse.ReviewResponse.ReviewImageResponse(
                id = id,
                url = url
            )
        }

        @Serializable
        data class ReviewKeywordResponseDto(
            @SerialName("id")
            val id: Int,
            @SerialName("content")
            val content: String,
            @SerialName("isPositive")
            val isPositive: Boolean,
        ) {
            fun toReviewKeywordResponse() =
                BlockReviewResponse.ReviewResponse.ReviewKeywordResponse(
                    id = id,
                    content = content,
                    isPositive = isPositive
                )
        }

        @Serializable
        data class ReviewMemberResponseDto(
            @SerialName("profileImage")
            val profileImage: String,
            @SerialName("nickname")
            val nickname: String,
            @SerialName("level")
            val level: Int,
        ) {
            fun toReviewMemberResponse() = BlockReviewResponse.ReviewResponse.ReviewMemberResponse(
                profileImage = profileImage, nickname = nickname, level = level
            )
        }

        @Serializable
        data class ReviewStadiumResponseDto(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
            @SerialName("mainImage")
            val mainImage: String,
            @SerialName("seatingChartImage")
            val seatingChartImage: String,
            @SerialName("labeledSeatingChartImage")
            val labeledSeatingChartImage: String,
            @SerialName("isActive")
            val isActive: Boolean,
        ) {
            fun toReviewStadiumResponse() =
                BlockReviewResponse.ReviewResponse.ReviewStadiumResponse(
                    id = id,
                    name = name,
                    mainImage = mainImage,
                    seatingChartImage = seatingChartImage,
                    labeledSeatingChartImage = labeledSeatingChartImage,
                    isActive = isActive
                )
        }

        @Serializable
        data class ReviewSectionResponseDto(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
            @SerialName("alias")
            val alias: String,
        ) {
            fun toReviewSectionResponse() =
                BlockReviewResponse.ReviewResponse.ReviewSectionResponse(
                    id = id,
                    name = name,
                    alias = alias
                )
        }

        @Serializable
        data class ReviewBlockResponseDto(
            @SerialName("id")
            val id: Int,
            @SerialName("code")
            val code: String,
        ) {
            fun toReviewBlockResponse() = BlockReviewResponse.ReviewResponse.ReviewBlockResponse(
                id = id,
                code = code
            )
        }

        @Serializable
        data class ReviewRowResponseDto(
            @SerialName("id")
            val id: Int,
            @SerialName("number")
            val number: Int,
        ) {
            fun toReviewRowResponse() = BlockReviewResponse.ReviewResponse.ReviewRowResponse(
                id = id,
                number = number
            )
        }

        @Serializable
        data class ReviewSeatResponseDto(
            @SerialName("id")
            val id: Int,
            @SerialName("seatNumber")
            val seatNumber: Int,
        ) {
            fun toReviewSeatResponse() = BlockReviewResponse.ReviewResponse.ReviewSeatResponse(
                id = id,
                seatNumber = seatNumber
            )
        }
    }

    @Serializable
    data class TopReviewImagesResponseDto(
        @SerialName("url")
        val url: String,
        @SerialName("reviewId")
        val reviewId: Int,
        @SerialName("blockCode")
        val blockCode: String,
        @SerialName("rowNumber")
        val rowNumber: Int,
        @SerialName("seatNumber")
        val seatNumber: Int,
    )

    @Serializable
    data class ReviewFilterResponseDto(
        @SerialName("rowNumber")
        val rowNumber: Int,
        @SerialName("seatNumber")
        val seatNumber: Int,
        @SerialName("year")
        val year: Int,
        @SerialName("month")
        val month: Int
    )
}
