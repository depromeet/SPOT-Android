package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.RecentReviewResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecentReviewDto(
    @SerialName("review")
    val review: ResponseReviewWrapperDto,
    @SerialName("reviewCount")
    val reviewCount: Int,
) {
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
        val images: List<ResponseImageDto>,
        @SerialName("keywords")
        val keywords: List<ResponseKeywordDto>,
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
    data class ResponseImageDto(
        @SerialName("id")
        val id: Int,
        @SerialName("url")
        val url: String,
    )

    @Serializable
    data class ResponseKeywordDto(
        @SerialName("id")
        val id: Int,
        @SerialName("content")
        val content: String,
        @SerialName("isPositive")
        val isPositive: Boolean,
    )

    companion object {
        fun ResponseRecentReviewDto.toRecentReviewResponse() = RecentReviewResponse(
            review = review.toReviewWrapperResponse(),
            reviewCount = reviewCount
        )

        fun ResponseReviewWrapperDto.toReviewWrapperResponse() =
            RecentReviewResponse.ReviewWrapperResponse(
                baseReview = baseReview.toReviewResponse(),
                stadiumName = stadiumName,
                sectionName = sectionName,
                blockCode = blockCode
            )

        fun ResponseReviewDto.toReviewResponse() = RecentReviewResponse.ReviewResponse(
            id = id,
            member = member.toMemberResponse(),
            stadium = stadium.toStadiumResponse(),
            section = section.toSectionResponse(),
            block = block.toBlockResponse(),
            row = row.toRowResponse(),
            seat = seat.toSeatResponse(),
            dateTime = dateTime,
            content = content,
            images = images.map { it.toImageResponse() },
            keywords = keywords.map { it.toKeywordResponse() }
        )

        fun ResponseMemberDto.toMemberResponse() = RecentReviewResponse.MemberResponse(
            profileImage = profileImage,
            nickname = nickname,
            level = level
        )

        fun ResponseStadiumDto.toStadiumResponse() = RecentReviewResponse.StadiumResponse(
            id = id,
            name = name
        )

        fun ResponseSectionDto.toSectionResponse() = RecentReviewResponse.SectionResponse(
            id = id,
            name = name,
            alias = alias
        )

        fun ResponseBlockDto.toBlockResponse() = RecentReviewResponse.BlockResponse(
            id = id,
            code = code
        )

        fun ResponseRowDto.toRowResponse() = RecentReviewResponse.RowResponse(
            id = id,
            number = number
        )

        fun ResponseSeatDto.toSeatResponse() = RecentReviewResponse.SeatResponse(
            id = id,
            seatNumber = seatNumber
        )

        fun ResponseImageDto.toImageResponse() = RecentReviewResponse.ImageResponse(
            id = id,
            url = url
        )

        fun ResponseKeywordDto.toKeywordResponse() = RecentReviewResponse.KeywordResponse(
            id = id,
            content = content,
            isPositive = isPositive
        )
    }
}