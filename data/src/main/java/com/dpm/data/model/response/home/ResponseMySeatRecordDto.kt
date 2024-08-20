package com.dpm.data.model.response.home

import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseMySeatRecordDto(
    @SerialName("memberInfoOnMyReview")
    val memberInfoOnMyReview: ResponseMemberDto,
    @SerialName("reviews")
    val reviews: List<ResponseReviewWrapperDto>,
    @SerialName("nextCursor")
    val NextCursor : String?,
    @SerialName("hasNext")
    val hasNext : Boolean,
    @SerialName("filter")
    val filter: ResponseFilterDto,
) {
    @Serializable
    data class ResponseMemberDto(
        @SerialName("userId")
        val userId: Int,
        @SerialName("profileImageUrl")
        val profileImageUrl: String?,
        @SerialName("level")
        val level: Int,
        @SerialName("levelTitle")
        val levelTitle: String,
        @SerialName("nickname")
        val nickname: String,
        @SerialName("reviewCount")
        val reviewCount: Int,
        @SerialName("teamId")
        val teamId: Int?,
        @SerialName("teamName")
        val teamName: String?,
    )


    @Serializable
    data class ResponseReviewWrapperDto(
        @SerialName("baseReview")
        val baseReview: ResponseReviewDto,
        @SerialName("stadiumName")
        val stadiumName: String,
        @SerialName("sectionName")
        val sectionName: String,
        @SerialName("blockCode")
        val blockCode: String,
    )

    @Serializable
    data class ResponseReviewDto(
        @SerialName("id")
        val id: Int,
        @SerialName("member")
        val member: ResponseMemberDto,
        @SerialName("stadium")
        val stadium: ResponseStadiumDto,
        @SerialName("section")
        val section: ResponseSectionDto,
        @SerialName("block")
        val block: ResponseBlockDto,
        @SerialName("row")
        val row: ResponseRowDto,
        @SerialName("seat")
        val seat: ResponseSeatDto?,
        @SerialName("dateTime")
        val dateTime: String,
        @SerialName("content")
        val content: String?,
        @SerialName("images")
        val images: List<ResponseReviewImageDto>,
        @SerialName("keywords")
        val keywords: List<ResponseReviewKeywordDto>,
    ) {
        @Serializable
        data class ResponseReviewKeywordDto(
            @SerialName("id")
            val id: Int,
            @SerialName("content")
            val content: String,
            @SerialName("isPositive")
            val isPositive: Boolean,
        )

        @Serializable
        data class ResponseMemberDto(
            @SerialName("profileImage")
            val profileImage: String?,
            @SerialName("nickname")
            val nickname: String,
            @SerialName("level")
            val level: Int,
        )

        @Serializable
        data class ResponseStadiumDto(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
        )

        @Serializable
        data class ResponseSectionDto(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
            @SerialName("alias")
            val alias: String?,
        )

        @Serializable
        data class ResponseBlockDto(
            @SerialName("id")
            val id: Int,
            @SerialName("code")
            val code: String,
        )

        @Serializable
        data class ResponseRowDto(
            @SerialName("id")
            val id: Int,
            @SerialName("number")
            val number: Int,
        )

        @Serializable
        data class ResponseSeatDto(
            @SerialName("id")
            val id: Int,
            @SerialName("seatNumber")
            val seatNumber: Int,
        )

        @Serializable
        data class ResponseReviewImageDto(
            @SerialName("id")
            val id: Int,
            @SerialName("url")
            val url: String,
        )
    }


    @Serializable
    data class ResponseFilterDto(
        @SerialName("rowNumber")
        val rowNumber: Int?,
        @SerialName("seatNumber")
        val seatNumber: Int?,
        @SerialName("year")
        val year: Int?,
        @SerialName("month")
        val month: Int?,
    )

    companion object {
        fun ResponseMySeatRecordDto.toMySeatRecordResponse() = ResponseMySeatRecord(
            profile = memberInfoOnMyReview.toMyProfileResponse(),
            reviews = reviews.map { it.baseReview.toReviewResponse() },
            nextCursor = NextCursor,
            hasNext = hasNext,
            isLoading = false
        )

        private fun ResponseMemberDto.toMyProfileResponse() =
            ResponseMySeatRecord.MyProfileResponse(
                userId = userId,
                profileImage = profileImageUrl ?: "",
                level = level,
                levelTitle = levelTitle,
                nickname = nickname,
                reviewCount = reviewCount,
                teamId = teamId,
                teamName = teamName
            )

        private fun ResponseReviewDto.toReviewResponse() = ResponseMySeatRecord.ReviewResponse(
            id = id,
            stadiumId = stadium.id,
            stadiumName = stadium.name,
            blockId = block.id,
            blockName = block.code,
            blockCode = block.code,
            rowId = row.id,
            rowNumber = row.number,
            seatId = seat?.id,
            seatNumber = seat?.seatNumber,
            date = dateTime,
            content = content ?: "",
            sectionName = section.name,
            member = member.toMemberResponse(),
            images = images.map { it.toReviewImageResponse() },
            keywords = keywords.map { it.toReviewKeywordResponse() }
        )

        private fun ResponseReviewDto.ResponseMemberDto.toMemberResponse() =
            ResponseMySeatRecord.ReviewResponse.MemberResponse(
                profileImage = profileImage ?: "",
                nickname = nickname,
                level = level
            )

        private fun ResponseReviewDto.ResponseReviewKeywordDto.toReviewKeywordResponse() =
            ResponseMySeatRecord.ReviewResponse.ReviewKeywordResponse(
                id = id,
                content = content,
                isPositive = isPositive
            )

        private fun ResponseReviewDto.ResponseReviewImageDto.toReviewImageResponse() =
            ResponseMySeatRecord.ReviewResponse.ReviewImageResponse(
                id = id,
                url = url
            )
    }
}
