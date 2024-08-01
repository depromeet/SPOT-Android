package com.depromeet.data.mapper

import com.depromeet.data.model.request.viewfinder.BlockReviewRequestQueryDto
import com.depromeet.data.model.response.viewfinder.BlockReviewResponseDto
import com.depromeet.data.model.response.viewfinder.BlockRowResponseDto
import com.depromeet.data.model.response.viewfinder.StadiumResponseDto
import com.depromeet.data.model.response.viewfinder.StadiumsResponseDto
import com.depromeet.domain.entity.request.viewfinder.BlockReviewRequestQuery
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.domain.entity.response.viewfinder.BlockRowResponse
import com.depromeet.domain.entity.response.viewfinder.StadiumResponse
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse

fun StadiumsResponseDto.toStadiumsResponse() = StadiumsResponse(
    id = id,
    name = name,
    homeTeams = homeTeams.map { it.toHomeTeamsResponse() },
    thumbnail = thumbnail,
    isActive = isActive
)

fun StadiumsResponseDto.HomeTeamsResponseDto.toHomeTeamsResponse() =
    StadiumsResponse.HomeTeamsResponse(
        id = id,
        alias = alias,
        color = color
    )


fun StadiumResponseDto.toStadiumResponse() = StadiumResponse(
    id = id,
    name = name,
    homeTeams = homeTeams.map { it.toHomeTeamsResponse() },
    thumbnail = thumbnail,
    stadiumUrl = seatChartWithLabel
)


fun BlockReviewResponseDto.toBlockReviewResponse() = BlockReviewResponse(
    location = location?.toLocationResponse() ?: BlockReviewResponse.LocationResponse(),
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

fun BlockReviewResponseDto.KeywordResponseDto.toKeywordResponse() =
    BlockReviewResponse.KeywordResponse(
        content = content,
        count = count,
        isPositive = isPositive
    )

fun BlockReviewResponseDto.TopReviewImagesResponseDto.toTopReviewImagesResponse() =
    BlockReviewResponse.TopReviewImagesResponse(
        url = url,
        reviewId = reviewId,
        blockCode = blockCode,
        rowNumber = rowNumber,
        seatNumber = seatNumber
    )

fun BlockReviewResponseDto.ReviewFilterResponseDto.toReviewFilterResponse() =
    BlockReviewResponse.ReviewFilterResponse(
        rowNumber = rowNumber ?: 0,
        seatNumber = seatNumber ?: 0,
        year = year ?: 0,
        month = month ?: 0
    )

fun BlockReviewResponseDto.ReviewResponseDto.toReviewResponse() =
    BlockReviewResponse.ReviewResponse(
        id = id,
        member = member.toReviewMemberResponse(),
        stadium = stadium.toReviewStadiumResponse(),
        section = section.toReviewSectionResponse(),
        block = block.toReviewBlockResponse(),
        row = row.toReviewRowResponse(),
        seat = seat.toReviewSeatResponse(),
        dateTime = dateTime,
        content = content ?: "",
        images = images.map { it.toReviewImageResponse() },
        keywords = keywords.map { it.toReviewKeywordResponse() }
    )

fun BlockRowResponseDto.toBlockRowResponse() = BlockRowResponse(
    id = id,
    code = code,
    rowInfo = rowInfo.map { it.toRowInfoResponse() }
)

fun BlockReviewRequestQuery.toBlockReviewRequestQueryDto() = BlockReviewRequestQueryDto(
    rowNumber = rowNumber,
    seatNumber = seatNumber,
    year = year,
    month = month,
    page = page,
    size = size
)
