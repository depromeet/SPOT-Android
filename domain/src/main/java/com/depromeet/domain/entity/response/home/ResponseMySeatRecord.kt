package com.depromeet.domain.entity.response.home

import com.depromeet.domain.entity.response.viewfinder.BASE
import com.depromeet.domain.entity.response.viewfinder.base

data class ResponseMySeatRecord(
    val profile: MyProfileResponse = MyProfileResponse(),
    val reviews: List<ReviewResponse> = emptyList(),
    val totalElements: Int = 0,
    val totalPages: Int = 0,
    val number: Int = 0,
    val size: Int = 0,
    val first: Boolean = true,
    val last: Boolean = true,
    val isLoading: Boolean = true,
) {
    data class MyProfileResponse(
        val userId: Int = 0,
        val profileImage: String = "",
        val level: Int = 0,
        val levelTitle: String = "",
        val nickname: String = "",
        val reviewCount: Int = 0,
        val teamId: Int? = null,
        val teamName: String? = "",
    )

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
    ) {
        fun formattedSeatName(): String {
            return "${formattedBaseName()} ${formattedSectionName()} ${formattedBlockName()} ${formattedRowNumber()} ${formattedSeatNumber()}"
        }

        private fun formattedBaseName(): String {
            return when (stadiumName.base(blockCode)) {
                BASE.Base1 -> "1루"
                BASE.Base3 -> "3루"
                else -> ""
            }
        }

        private fun formattedSectionName(): String {
            val sectionNameSplits = sectionName.split("\n")
            var section = if (sectionNameSplits.size >= 2) {
                sectionNameSplits[0] + " " + sectionNameSplits[1]
            } else {
                sectionName.trim()
            }
            return section
        }

        private fun formattedBlockName() = blockCode.replace("w", "") + "블록"


        private fun formattedRowNumber() = "${rowNumber}열"

        private fun formattedSeatNumber() = when (seatNumber) {
            null -> ""
            else -> "${seatNumber}번"
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
