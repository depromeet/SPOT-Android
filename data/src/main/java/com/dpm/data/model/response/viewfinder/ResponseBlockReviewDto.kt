package com.dpm.data.model.response.viewfinder

import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseBlockReviewDto(
    @SerialName("location")
    val location: ResponseLocationDto?,
    @SerialName("keywords")
    val keywords: List<ResponseKeywordDto>,
    @SerialName("reviews")
    val reviews: List<ResponseReviewDto>,
    @SerialName("topReviewImages")
    val topReviewImages: List<ResponseTopReviewImagesDto>,
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
    val filter: ResponseReviewFilterDto
) {
    @Serializable
    data class ResponseLocationDto(
        @SerialName("stadiumName")
        val stadiumName: String,
        @SerialName("sectionName")
        val sectionName: String,
        @SerialName("blockCode")
        val blockCode: String
    )

    @Serializable
    data class ResponseKeywordDto(
        @SerialName("content")
        val content: String,
        @SerialName("count")
        val count: Int,
        @SerialName("isPositive")
        val isPositive: Boolean
    )

    @Serializable
    data class ResponseReviewDto(
        @SerialName("id")
        val id: Long,
        @SerialName("member")
        val member: ResponseReviewMemberDto,
        @SerialName("stadium")
        val stadium: ResponseReviewStadiumDto,
        @SerialName("section")
        val section: ResponseReviewSectionDto,
        @SerialName("block")
        val block: ResponseReviewBlockDto,
        @SerialName("row")
        val row: ResponseReviewRowDto,
        @SerialName("seat")
        val seat: ResponseReviewSeatDto?,
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
        data class ResponseReviewImageDto(
            @SerialName("id")
            val id: Int,
            @SerialName("url")
            val url: String,
        )

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
        data class ResponseReviewMemberDto(
            @SerialName("profileImage")
            val profileImage: String?,
            @SerialName("nickname")
            val nickname: String,
            @SerialName("level")
            val level: Int,
        )

        @Serializable
        data class ResponseReviewStadiumDto(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String
        )

        @Serializable
        data class ResponseReviewSectionDto(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
            @SerialName("alias")
            val alias: String?,
        )

        @Serializable
        data class ResponseReviewBlockDto(
            @SerialName("id")
            val id: Int,
            @SerialName("code")
            val code: String,
        )

        @Serializable
        data class ResponseReviewRowDto(
            @SerialName("id")
            val id: Int,
            @SerialName("number")
            val number: Int,
        )

        @Serializable
        data class ResponseReviewSeatDto(
            @SerialName("id")
            val id: Int?,
            @SerialName("seatNumber")
            val seatNumber: Int?,
        )
    }

    @Serializable
    data class ResponseTopReviewImagesDto(
        @SerialName("url")
        val url: String,
        @SerialName("reviewId")
        val reviewId: Int,
        @SerialName("blockCode")
        val blockCode: String,
        @SerialName("rowNumber")
        val rowNumber: Int,
        @SerialName("seatNumber")
        val seatNumber: Int?,
    )

    @Serializable
    data class ResponseReviewFilterDto(
        @SerialName("rowNumber")
        val rowNumber: Int?,
        @SerialName("seatNumber")
        val seatNumber: Int?,
        @SerialName("year")
        val year: Int?,
        @SerialName("month")
        val month: Int?
    )
}

fun ResponseBlockReviewDto.toBlockReviewResponse() = ResponseBlockReview(
    location = location?.toLocationResponse() ?: ResponseBlockReview.ResponseLocation(),
    keywords = keywords.map { it.toKeywordResponse() },
    reviews = reviews.map { it.toReviewResponse() },
    topReviewImages = topReviewImages.map { it.toTopReviewImagesResponse() },
    totalElements = totalElements,
    totalPages = totalPages,
    number = number,
    size = size,
    first = first,
    last = last,
    filter = filter.toReviewFilterResponse()
)

fun ResponseBlockReviewDto.ResponseKeywordDto.toKeywordResponse() =
    ResponseBlockReview.ResponseKeyword(
        content = content,
        count = count,
        isPositive = isPositive
    )

fun ResponseBlockReviewDto.ResponseTopReviewImagesDto.toTopReviewImagesResponse() =
    ResponseBlockReview.ResponseTopReviewImages(
        url = url,
        reviewId = reviewId,
        blockCode = blockCode,
        rowNumber = rowNumber,
        seatNumber = seatNumber ?: 0
    )

fun ResponseBlockReviewDto.ResponseReviewFilterDto.toReviewFilterResponse() =
    ResponseBlockReview.ResponseReviewFilter(
        rowNumber = rowNumber ?: 0,
        seatNumber = seatNumber ?: 0,
        year = year ?: 0,
        month = month ?: 0
    )

fun ResponseBlockReviewDto.ResponseReviewDto.toReviewResponse() =
    ResponseBlockReview.ResponseReview(
        id = id,
        member = member.toReviewMemberResponse(),
        stadium = stadium.toReviewStadiumResponse(),
        section = section.toReviewSectionResponse(),
        block = block.toReviewBlockResponse(),
        row = row.toReviewRowResponse(),
        seat = seat?.toReviewSeatResponse() ?: ResponseBlockReview.ResponseReview.ResponseReviewSeat(),
        dateTime = dateTime,
        content = content ?: "",
        images = images.map { it.toReviewImageResponse() },
        keywords = keywords.map { it.toReviewKeywordResponse() }
    )

fun ResponseBlockReviewDto.ResponseLocationDto.toLocationResponse() =
    ResponseBlockReview.ResponseLocation(
        stadiumName = stadiumName,
        sectionName = sectionName,
        blockCode = blockCode
    )

fun ResponseBlockReviewDto.ResponseReviewDto.ResponseReviewImageDto.toReviewImageResponse() =
    ResponseBlockReview.ResponseReview.ResponseReviewImage(
        id = id,
        url = url
    )

fun ResponseBlockReviewDto.ResponseReviewDto.ResponseReviewKeywordDto.toReviewKeywordResponse() =
    ResponseBlockReview.ResponseReview.ResponseReviewKeyword(
        id = id,
        content = content,
        isPositive = isPositive
    )

fun ResponseBlockReviewDto.ResponseReviewDto.ResponseReviewMemberDto.toReviewMemberResponse() =
    ResponseBlockReview.ResponseReview.ResponseReviewMember(
        profileImage = profileImage ?: "", nickname = nickname, level = level
    )

fun ResponseBlockReviewDto.ResponseReviewDto.ResponseReviewStadiumDto.toReviewStadiumResponse() =
    ResponseBlockReview.ResponseReview.ResponseReviewStadium(
        id = id,
        name = name
    )

fun ResponseBlockReviewDto.ResponseReviewDto.ResponseReviewSectionDto.toReviewSectionResponse() =
    ResponseBlockReview.ResponseReview.ResponseReviewSection(
        id = id,
        name = name,
        alias = alias ?: ""
    )

fun ResponseBlockReviewDto.ResponseReviewDto.ResponseReviewBlockDto.toReviewBlockResponse() =
    ResponseBlockReview.ResponseReview.ResponseReviewBlock(
        id = id,
        code = code
    )

fun ResponseBlockReviewDto.ResponseReviewDto.ResponseReviewRowDto.toReviewRowResponse() =
    ResponseBlockReview.ResponseReview.ResponseReviewRow(
        id = id,
        number = number
    )

fun ResponseBlockReviewDto.ResponseReviewDto.ResponseReviewSeatDto.toReviewSeatResponse() =
    ResponseBlockReview.ResponseReview.ResponseReviewSeat(
        id = id ?: 0,
        seatNumber = seatNumber ?: 0
    )

