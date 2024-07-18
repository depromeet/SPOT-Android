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
        color = color.toColorResponse()
    )

fun StadiumsResponseDto.HomeTeamsResponseDto.ColorResponseDto.toColorResponse() =
    StadiumsResponse.HomeTeamsResponse.ColorResponse(
        red = red, green = green, blue = blue
    )

fun StadiumResponseDto.toStadiumResponse() = StadiumResponse(
    id = id,
    name = name,
    homeTeams = homeTeams.map { it.toHomeTeamsResponse() },
    thumbnail = thumbnail,
    stadiumUrl = seatChartWithLabel
)

fun BlockReviewResponseDto.toBlockReviewResponse() = BlockReviewResponse(
    keywords = keywords.map { it.toKeywordResponse() },
    reviews = reviews.map { it.toReviewResponse() },
    totalCount = totalCount,
    filteredCount = filteredCount,
    offset = offset,
    limit = limit,
    hasMore = hasMore,
    filter = filter.toReviewFilterResponse()
)

fun BlockReviewResponseDto.KeywordResponseDto.toKeywordResponse() =
    BlockReviewResponse.KeywordResponse(
        content = content,
        count = count
    )

fun BlockReviewResponseDto.ReviewResponseDto.toReviewResponse() =
    BlockReviewResponse.ReviewResponse(
        id = id,
        userId = userId,
        blockId = blockId,
        seatId = seatId,
        rowId = rowId,
        seatNumber = seatNumber,
        date = date,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        images = images.map { it.toReviewImageResponse() },
        keywords = keywords.map { it.toReviewKeywordResponse() }
    )

fun BlockReviewResponseDto.ReviewFilterResponseDto.toReviewFilterResponse() =
    BlockReviewResponse.ReviewFilterResponse(
        rowId = rowId,
        seatNumber = seatNumber
    )

fun BlockReviewResponseDto.ReviewResponseDto.ReviewImageResponseDto.toReviewImageResponse() =
    BlockReviewResponse.ReviewResponse.ReviewImageResponse(
        id = id,
        url = url
    )

fun BlockReviewResponseDto.ReviewResponseDto.ReviewKeywordResponseDto.toReviewKeywordResponse() =
    BlockReviewResponse.KeywordResponse(
        content = content,
        isPositive = isPositive,
    )

fun BlockRowResponseDto.toBlockRowResponse() = BlockRowResponse(
    id = id,
    code = code,
    rowInfo = rowInfo.map { it.toRowInfoResponse() }
)

fun BlockRowResponseDto.RowInfoResponseDto.toRowInfoResponse() = BlockRowResponse.RowInfoResponse(
    id = id,
    number = number,
    minSeatNum = minSeatNum,
    maxSeatNum = maxSeatNum
)

fun BlockReviewRequestQuery.toBlockReviewRequestQueryDto() = BlockReviewRequestQueryDto(
    rowId = rowId,
    seatNumber = seatNumber,
    offset = offset,
    limit = limit
)