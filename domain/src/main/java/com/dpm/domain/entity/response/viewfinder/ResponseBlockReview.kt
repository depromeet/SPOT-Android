package com.dpm.domain.entity.response.viewfinder

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class BASE {
    Base1, Base3, Nothing
}

fun String.base(blockCode: String): BASE {
    when (this) {
        "서울 잠실 야구장" -> {
            return when {
                blockCode in listOf("301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317") // 1루 네이비석
                        + listOf("401","402","403","404","405","406","407","408","409","410","411") // 1루 외야그린석
                        + listOf("101","102","103","104","105","106","201","202","203","204") // 1루 레드석
                        + listOf("205","206","207","208") // 1루 오렌지석
                        + listOf("107","108","109","209","210","211") // 1루 블루석
                        + listOf("110","111","212","213") // 1루 테이블석
                        + listOf("101w", "102w", "109w") // 1루 휠체어석
                -> BASE.Base1
                blockCode in listOf("exciting1", "exciting3", "premium") -> BASE.Nothing
                else -> BASE.Base3
            }
        }
    }
    return BASE.Nothing
}

data class ResponseBlockReview(
    val location: ResponseLocation = ResponseLocation(),
    val keywords: List<ResponseKeyword> = emptyList(),
    val reviews: List<ResponseReview> = emptyList(),
    val topReviewImages: List<ResponseReview> = emptyList(),
    val totalElements: Long = 0,
    val nextCursor: String = "",
    val hasNext: Boolean = false,
    val filter: ResponseReviewFilter = ResponseReviewFilter()
) {
    data class ResponseLocation(
        val stadiumName: String = "",
        val sectionName: String = "",
        val blockCode: String = ""
    )

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
        val likesCount: Long,
        val scrapsCount: Long,
        val reviewType: String,
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
            val id: Int = 0,
            val number: Int = 0,
        )

        data class ResponseReviewSeat(
            val id: Int = 0,
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
    }

    data class ResponseReviewFilter(
        val rowNumber: Int = 0,
        val seatNumber: Int = 0,
        val year: Int = 0,
        val month: Int = 0
    )
}
