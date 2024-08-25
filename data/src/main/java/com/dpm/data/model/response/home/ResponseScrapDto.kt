package com.dpm.data.model.response.home

import com.dpm.domain.entity.response.home.ResponseScrap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseScrapDto(
    @SerialName("reviews")
    val reviews: List<ResponseReviewWrapperDto>,
    @SerialName("nextCursor")
    val nextCursor: String?,
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("totalScrapCount")
    val totalScrapCount: Int,
    @SerialName("filter")
    val filter: ResponseFilterDto,
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
        val seat: ResponseSeatDto?,
        @SerialName("dateTime")
        val dateTime: String,
        @SerialName("content")
        val content: String,
        @SerialName("images")
        val images: List<ResponseImageDto>,
        @SerialName("keywords")
        val keywords: List<ResponseKeywordDto>,
        @SerialName("likesCount")
        val likesCount: Int,
        @SerialName("scrapsCount")
        val scrapsCount: Int,
        @SerialName("reviewType")
        val reviewType: String?,
        @SerialName("isLiked")
        val isLiked: Boolean,
        @SerialName("isScrapped")
        val isScrapped: Boolean,
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

    @Serializable
    data class ResponseFilterDto(
        @SerialName("stadiumId")
        val stadiumId: Int,
        @SerialName("months")
        val months: List<Int>,
        @SerialName("good")
        val good: List<String>,
        @SerialName("bad")
        val bad: List<String>,
    )
}

fun ResponseScrapDto.toResponseScrap(): ResponseScrap = ResponseScrap(
    reviews = reviews.map { it.toResponseReviewWrapper() },
    nextCursor = nextCursor,
    hasNext = hasNext,
    totalScrapCount = totalScrapCount,
    filter = filter.toResponseFilter()
)

fun ResponseScrapDto.ResponseReviewWrapperDto.toResponseReviewWrapper(): ResponseScrap.ResponseReviewWrapper =
    ResponseScrap.ResponseReviewWrapper(
        baseReview = baseReview.toResponseBaseReview(),
        stadiumName = stadiumName,
        sectionName = sectionName,
        blockCode = blockCode
    )

fun ResponseScrapDto.ResponseReviewDto.toResponseBaseReview(): ResponseScrap.ResponseBaseReview =
    ResponseScrap.ResponseBaseReview(
        id = id,
        member = member.toResponseMember(),
        stadium = stadium.toResponseStadium(),
        section = section.toResponseSection(),
        block = block.toResponseBlock(),
        row = row.toResponseRow(),
        seat = seat?.toResponseSeat(),
        dateTime = dateTime,
        content = content,
        images = images.map { it.toResponseImage() },
        keywords = keywords.map { it.toResponseKeyword() },
        likesCount = likesCount,
        scrapsCount = scrapsCount,
        reviewType = reviewType,
        isLiked = isLiked,
        isScrapped = isScrapped
    )

fun ResponseScrapDto.ResponseMemberDto.toResponseMember(): ResponseScrap.ResponseMember =
    ResponseScrap.ResponseMember(
        profileImage = profileImage ?: "",
        nickname = nickname,
        level = level
    )

fun ResponseScrapDto.ResponseStadiumDto.toResponseStadium(): ResponseScrap.ResponseStadium =
    ResponseScrap.ResponseStadium(
        id = id,
        name = name
    )

fun ResponseScrapDto.ResponseSectionDto.toResponseSection(): ResponseScrap.ResponseSection =
    ResponseScrap.ResponseSection(
        id = id,
        name = name,
        alias = alias ?: ""
    )

fun ResponseScrapDto.ResponseBlockDto.toResponseBlock(): ResponseScrap.ResponseBlock =
    ResponseScrap.ResponseBlock(
        id = id,
        code = code.replace("w", "")
    )

fun ResponseScrapDto.ResponseRowDto.toResponseRow(): ResponseScrap.ResponseRow =
    ResponseScrap.ResponseRow(
        id = id,
        number = number
    )

fun ResponseScrapDto.ResponseSeatDto.toResponseSeat(): ResponseScrap.ResponseSeat =
    ResponseScrap.ResponseSeat(
        id = id,
        seatNumber = seatNumber
    )

fun ResponseScrapDto.ResponseImageDto.toResponseImage(): ResponseScrap.ResponseImage =
    ResponseScrap.ResponseImage(
        id = id,
        url = url
    )

fun ResponseScrapDto.ResponseKeywordDto.toResponseKeyword(): ResponseScrap.ResponseKeyword =
    ResponseScrap.ResponseKeyword(
        id = id,
        content = content,
        isPositive = isPositive
    )

fun ResponseScrapDto.ResponseFilterDto.toResponseFilter(): ResponseScrap.ResponseFilter =
    ResponseScrap.ResponseFilter(
        stadiumId = stadiumId,
        months = months,
        good = good,
        bad = bad
    )