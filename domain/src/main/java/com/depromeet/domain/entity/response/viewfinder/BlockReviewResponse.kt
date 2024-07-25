package com.depromeet.domain.entity.response.viewfinder

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class BlockReviewResponse(
    val location: LocationResponse = LocationResponse(),
    val keywords: List<KeywordResponse> = emptyList(),
    val reviews: List<ReviewResponse> = emptyList(),
    val topReviewImages: List<TopReviewImagesResponse> = emptyList(),
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val number: Int = 0,
    val size: Int = 0,
    val first: Boolean = false,
    val last: Boolean = false,
    val filter: ReviewFilterResponse = ReviewFilterResponse()
) {
    data class LocationResponse(
        val stadiumName: String = "",
        val sectionName: String = "",
        val blockCode: String = ""
    )

    data class KeywordResponse(
        val content: String = "",
        val count: Int = 0,
        val isPositive: Boolean = false
    )

    data class ReviewResponse(
        val id: Long,
        val member: ReviewMemberResponse = ReviewMemberResponse(),
        val stadium: ReviewStadiumResponse,
        val section: ReviewSectionResponse,
        val block: ReviewBlockResponse,
        val row: ReviewRowResponse,
        val seat: ReviewSeatResponse,
        val dateTime: String = "",
        val content: String = "",
        val images: List<ReviewImageResponse> = emptyList(),
        val keywords: List<ReviewKeywordResponse> = emptyList(),

        ) {
        data class ReviewImageResponse(
            val id: Int,
            val url: String = "",
        )

        data class ReviewKeywordResponse(
            val id: Int,
            val content: String = "",
            val isPositive: Boolean = false,
        )

        data class ReviewMemberResponse(
            val profileImage: String = "",
            val nickname: String = "",
            val level: Int = 0,
        )

        data class ReviewStadiumResponse(
            val id: Int,
            val name: String = ""
        )

        data class ReviewSectionResponse(
            val id: Int,
            val name: String = "",
            val alias: String = "",
        )

        data class ReviewBlockResponse(
            val id: Int,
            val code: String = "",
        )

        data class ReviewRowResponse(
            val id: Int,
            val number: Int = 0,
        )

        data class ReviewSeatResponse(
            val id: Int,
            val seatNumber: Int = 0,
        )

        /**
         * @example : 2024-07-23T12:18:21.744Z -> 7월 23일
         */
        fun formattedDate(): String {
            val formatter = DateTimeFormatter.ofPattern("M월d일")
            val dateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
            return dateTime.format(formatter)
        }

        /**
         * @example : 207블록 1열 12번
         */
        fun formattedNumber(): String {
            var numberString = ""
            if (block.code.isNotEmpty()) numberString += "${block.code}블록 "
            if (row.number > 0) numberString += "${row.number}열 "
            if (seat.seatNumber > 0) numberString += "${seat.seatNumber}번"
            return numberString
        }
    }

    data class TopReviewImagesResponse(
        val url: String = "",
        val reviewId: Int,
        val blockCode: String = "",
        val rowNumber: Int = 0,
        val seatNumber: Int = 0,
    ) {
        /**
         * @example : 207블록 1열 12번
         */
        fun formattedNumber(): String {
            var numberString = ""
            if (blockCode.isNotEmpty()) numberString += "${blockCode}블록 "
            if (rowNumber > 0) numberString += "${rowNumber}열 "
            if (seatNumber > 0) numberString += "${seatNumber}번"
            return numberString
        }
    }

    data class ReviewFilterResponse(
        val rowNumber: Int = 0,
        val seatNumber: Int = 0,
        val year: Int = 0,
        val month: Int = 0
    )

    /**
     * @example : 서울 잠실 야구장
     */
    fun formattedStadiumTitle(): String {
        return reviews.getOrNull(0)?.stadium?.name ?: ""
    }

    /**
     * @example : 오렌지석•207블록
     */
    fun formattedStadiumBlock(): String {
        return location.sectionName + "•" + location.blockCode + "블록"
    }
}
