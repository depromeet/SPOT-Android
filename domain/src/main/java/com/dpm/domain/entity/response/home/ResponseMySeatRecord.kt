package com.dpm.domain.entity.response.home

import com.dpm.domain.entity.response.viewfinder.BASE
import com.dpm.domain.entity.response.viewfinder.base

data class ResponseMySeatRecord(
    val reviews: List<ReviewResponse> = emptyList(),
    val nextCursor: String? = null,
    val hasNext: Boolean = false,
    val isLoading: Boolean = true,
) {

    data class ReviewResponse(
        val id: Int,
        val stadiumId: Int,
        val stadiumName: String = "",
        val blockId: Int = 0,
        val blockName: String = "",
        val blockCode: String = "",
        val rowId: Int = 0,
        val rowNumber: Int = 0,
        val seatId: Int? = null,
        val seatNumber: Int? = null,
        val date: String = "",
        val content: String = "",
        val sectionName: String = "",
        val member: MemberResponse = MemberResponse(),
        val images: List<ReviewImageResponse> = emptyList(),
        val keywords: List<ReviewKeywordResponse> = emptyList(),
        val likesCount: Int = 0,
        val scrapsCount: Int = 0,
        val reviewType: String = "",
        val isLiked: Boolean = false,
        val isScrapped: Boolean = false,
    ) {
        fun formattedLevel(): String = "Lv.${member.level}"

        fun formattedSeatName(): String {
            return "${formattedBaseName()}${formattedSectionName()}${formattedBlockName()}${formattedRowNumber()}${formattedSeatNumber()}"
        }

        fun kakaoShareSeatFeedTitle() : String {
            val base = when(stadiumName.base(blockCode)) {
                BASE.Base1 -> "1루 "
                BASE.Base3 -> "3루 "
                else -> ""
            }
            val section = sectionName
            val block = "${rowNumber} 열 "
            val seat = if(seatNumber == null) "" else "${seatNumber}번 "

            return "$stadiumName $base $section $block $seat".trim()
        }

        private fun formattedBaseName(): String {
            return when (stadiumName.base(blockCode)) {
                BASE.Base1 -> "1루 "
                BASE.Base3 -> "3루 "
                else -> ""
            }
        }

        private fun formattedSectionName(): String {

            val sectionNameSplits = sectionName.split("\n")
            val section = if (sectionNameSplits.size >= 2) {
                sectionNameSplits[0] + " " + sectionNameSplits[1]
            } else {
                sectionName.trim()
            }
            return when(blockCode){
                in listOf("101w", "102w", "122w", "121w", "109w", "114w","exciting1","exciting3","premium") -> ""
                else -> section
            }
        }

        private fun formattedBlockName() = when(stadiumId) {
            1 -> {
                when(blockCode) {
                    in listOf("101w", "102w", "122w", "121w", "109w", "114w") -> "휠체어석 ${blockCode.replace("w", "")}블록 "
                    in listOf("exciting1") -> "1루 익사이팅석 "
                    in listOf("exciting3") -> "3루 익사이팅석 "
                    in listOf("premium") -> "프리미엄석 "
                    else -> "${blockCode}블록 "
                }
            }
            else -> ""
        }

        private fun formattedRowNumber() = "${rowNumber}열 "

        private fun formattedSeatNumber() = when (seatNumber) {
            null -> ""
            else -> "${seatNumber}번 "
        }

        data class ReviewImageResponse(
            val id: Int = 0,
            val url: String = "",
        )

        data class ReviewKeywordResponse(
            val id: Int = 0,
            val content: String = "",
            val isPositive: Boolean = false,
        )

        data class MemberResponse(
            val profileImage: String = "",
            val nickname: String = "",
            val level: Int = 0,
        )
    }

}
