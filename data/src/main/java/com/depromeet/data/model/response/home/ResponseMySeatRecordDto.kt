package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseMySeatRecordDto(
    @SerialName("memberInfoOnMyReview")
    val memberInfoOnMyReview: ResponseMemberDto,
    @SerialName("reviews")
    val reviews: List<ResponseReviewWrapperDto>,
    @SerialName("totalElements")
    val totalElements: Int,
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
    val filter: ResponseFilterDto,
) {
    @Serializable
    data class ResponseMemberDto(
        @SerialName("userId")
        val userId: Int,
        @SerialName("profileImageUrl")
        val profileImageUrl: String,
        @SerialName("level")
        val level: Int,
        @SerialName("nickname")
        val nickname: String,
        @SerialName("reviewCount")
        val reviewCount: Int,
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
        val seat: ResponseSeatDto,
        @SerialName("dateTime")
        val dateTime: String,
        @SerialName("content")
        val content: String,
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
            val profileImage: String,
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
            @SerialName("mainImage")
            val mainImage: String,
            @SerialName("seatingChartImage")
            val seatingChartImage: String,
            @SerialName("labeledSeatingChartImage")
            val labeledSeatingChartImage: String,
            @SerialName("isActive")
            val isActive: Boolean,
        )

        @Serializable
        data class ResponseSectionDto(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
            @SerialName("alias")
            val alias: String,
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
    }

    @Serializable
    data class ResponseReviewImageDto(
        @SerialName("id")
        val id: Int,
        @SerialName("url")
        val url: String,
    )

    @Serializable
    data class ResponseFilterDto(
        @SerialName("rowNumber")
        val rowNumber: Int,
        @SerialName("seatNumber")
        val seatNumber: Int,
        @SerialName("year")
        val year: Int,
        @SerialName("month")
        val month: Int,
    )

    companion object {
        fun ResponseMySeatRecordDto.toMySeatRecordResponse() = MySeatRecordResponse(
            profile = memberInfoOnMyReview.toMyProfileResponse(),
            reviews = reviews.map { it.baseReview.toReviewResponse() },
            totalElements = totalElements,
            totalPages = totalPages,
            number = number,
            size = size,
        )

        private fun ResponseMemberDto.toMyProfileResponse() =
            MySeatRecordResponse.MyProfileResponse(
                userId = userId,
                profileImage = profileImageUrl,
                level = level,
                nickname = nickname,
                reviewCount = reviewCount
            )

        private fun ResponseReviewDto.toReviewResponse() = MySeatRecordResponse.ReviewResponse(
            id = id,
            stadiumId = stadium.id,
            stadiumName = stadium.name,
            blockId = block.id,
            blockName = block.code,
            seatId = seat.id,
            rowId = row.id,
            rowNumber = row.number,
            seatNumber = seat.seatNumber,
            date = dateTime,
            content = content,
            member = member.toMemberResponse(),
            images = images.map { it.toReviewImageResponse() },
            keywords = keywords.map { it.toReviewKeywordResponse() }
        )

        private fun ResponseReviewDto.ResponseMemberDto.toMemberResponse() =
            MySeatRecordResponse.ReviewResponse.MemberResponse(
                profileImage = profileImage,
                nickname = nickname,
                level = level
            )

        private fun ResponseReviewDto.ResponseReviewKeywordDto.toReviewKeywordResponse() =
            MySeatRecordResponse.ReviewResponse.ReviewKeywordResponse(
                id = id,
                content = content,
                isPositive = isPositive
            )

        private fun ResponseReviewImageDto.toReviewImageResponse() =
            MySeatRecordResponse.ReviewResponse.ReviewImageResponse(
                id = id,
                url = url
            )
    }
}
