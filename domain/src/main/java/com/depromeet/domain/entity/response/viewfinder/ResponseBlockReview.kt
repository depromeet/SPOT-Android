package com.depromeet.domain.entity.response.viewfinder

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ResponseBlockReview(
    val location: ResponseLocation = ResponseLocation(),
    val keywords: List<ResponseKeyword> = emptyList(),
    val reviews: List<ResponseReview> = emptyList(),
    val topReviewImages: List<ResponseTopReviewImages> = emptyList(),
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val number: Int = 0,
    val size: Int = 0,
    val first: Boolean = false,
    val last: Boolean = false,
    val filter: ResponseReviewFilter = ResponseReviewFilter()
) {
    data class ResponseLocation(
        val stadiumName: String = "",
        val sectionName: String = "",
        val blockCode: String = ""
    ) {
        /**
         * @example : 오렌지석•207블록
         */
        fun formattedStadiumBlock(): String {
            val sectionNameSplits = sectionName.split("\n")
            var section = if (sectionNameSplits.size >= 2) {
                sectionNameSplits[0] + " " + sectionNameSplits[1]
            } else {
                sectionName.trim()
            }
            return section + "•" + blockCode + "블록"
        }

        fun toTitle(): String {
            val sectionNameSplits = sectionName.split("\n")
            var section = if (sectionNameSplits.size >= 2) {
                sectionNameSplits[0] + " " + sectionNameSplits[1]
            } else {
                sectionName.trim()
            }
            return "$stadiumName $section"
        }
    }

    data class ResponseKeyword(
        val content: String = "",
        val count: Int = 0,
        val isPositive: Boolean = false
    )

    data class ResponseReview(
        val id: Long,
        val member: ResponseReviewMember = ResponseReviewMember(),
        val stadium: ResponseReviewStadium,
        val section: ResponseReviewSection,
        val block: ResponseReviewBlock,
        val row: ResponseReviewRow,
        val seat: ResponseReviewSeat,
        val dateTime: String = "",
        val content: String = "",
        val images: List<ResponseReviewImage> = emptyList(),
        val keywords: List<ResponseReviewKeyword> = emptyList(),

        ) {
        data class ResponseReviewImage(
            val id: Int,
            val url: String = "",
        )

        data class ResponseReviewKeyword(
            val id: Int,
            val content: String = "",
            val isPositive: Boolean = false,
        )

        data class ResponseReviewMember(
            val profileImage: String = "",
            val nickname: String = "",
            val level: Int = 0,
        )

        data class ResponseReviewStadium(
            val id: Int,
            val name: String = ""
        )

        data class ResponseReviewSection(
            val id: Int,
            val name: String = "",
            val alias: String = "",
        )

        data class ResponseReviewBlock(
            val id: Int,
            val code: String = "",
        )

        data class ResponseReviewRow(
            val id: Int,
            val number: Int = 0,
        )

        data class ResponseReviewSeat(
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

    data class ResponseTopReviewImages(
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

    data class ResponseReviewFilter(
        val rowNumber: Int = 0,
        val seatNumber: Int = 0,
        val year: Int = 0,
        val month: Int = 0
    )
}
