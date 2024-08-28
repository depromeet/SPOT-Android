package com.dpm.domain.entity.response.home

import com.dpm.domain.entity.response.viewfinder.BASE
import com.dpm.domain.entity.response.viewfinder.base

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
        val reviewType: String? = "",
        val isLiked: Boolean = false,
        val isScrapped: Boolean = false,
    ) {
        fun formattedStadiumToSection() : String =
            "${stadium.name} ${formattedBaseName()} ${formattedSectionName()}".trim()

        fun formattedBlockToSeat(): String =
            if (seat != null) {
                "${formattedBlockName()} ${row.number}열 ${seat.seatNumber}번 "
            } else {
                "${formattedBlockName()} ${row.number}열 "
            }

        fun kakaoShareSeatFeedTitle() : String {
            val base = when(stadium.name.base(block.code)) {
                BASE.Base1 -> "1루 "
                BASE.Base3 -> "3루 "
                else -> ""
            }
            val section = section.name
            val block = "${row.number}열 "
            val seat = if(seat == null) "" else "${seat.seatNumber}번 "

            return "${stadium.name}$base$section$block$seat".trim()
        }

        fun formattedBaseToBlock() : String =
            "${formattedBaseName()}${formattedSectionName()}${formattedBlockName()}".trim()

        private fun formattedBaseName() : String {
            return when (stadium.name.base(block.code)){
                BASE.Base1 -> "1루 "
                BASE.Base3 -> "3루 "
                else -> ""
            }
        }



        private fun formattedSectionName(): String {
            val sectionNameSplits = section.name.split("\n")
            val section = if (sectionNameSplits.size >= 2) {
                sectionNameSplits[0] + " " + sectionNameSplits[1]
            } else {
                section.name.trim()
            }
            return when(block.code){
                in listOf("101w", "102w", "122w", "121w", "109w", "114w","exciting1","exciting3","premium") -> ""
                else -> section
            }
        }

        private fun formattedBlockName() = when(stadium.id) {
            1 -> {
                when(block.code) {
                    in listOf("101w", "102w", "122w", "121w", "109w", "114w") -> "휠체어석 ${block.code.replace("w", "")}블록 "
                    in listOf("exciting1") -> "1루 익사이팅석 "
                    in listOf("exciting3") -> "3루 익사이팅석 "
                    in listOf("premium") -> "프리미엄석 "
                    else -> "${block.code}블록 "
                }
            }
            else -> ""
        }

    }

    data class ResponseMember(
        val profileImage: String? = null,
        val nickname: String = "",
        val level: Int = 0,
    ) {
        fun formattedLevel(): String = "Lv.$level"
    }

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