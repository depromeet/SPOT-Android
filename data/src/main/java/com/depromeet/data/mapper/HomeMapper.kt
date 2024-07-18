package com.depromeet.data.mapper

import com.depromeet.data.model.request.RequestMySeatRecordDto
import com.depromeet.data.model.response.home.ResponseBaseballTeamDto
import com.depromeet.data.model.response.home.ResponseMySeatRecordDto
import com.depromeet.domain.entity.request.home.MySeatRecordRequest
import com.depromeet.domain.entity.response.home.BaseballTeamResponse
import com.depromeet.domain.entity.response.home.MySeatRecordResponse

fun ResponseMySeatRecordDto.toMySeatRecordResponse() = MySeatRecordResponse(
    reviews = reviews.map { it.toReviewResponse() },
    totalCount = totalCount,
    filteredCount = filteredCount,
    offset = offset,
    limit = limit,
    hasMore = hasMore,
    filter = filter.toFilterResponse()
)

fun ResponseMySeatRecordDto.ResponseFilterDto.toFilterResponse() =
    MySeatRecordResponse.FilterResponse(
        stadiumId = stadiumId,
        year = year,
        month = month
    )


fun ResponseMySeatRecordDto.ResponseReviewDto.toReviewResponse() =
    MySeatRecordResponse.ReviewResponse(
        id = id,
        stadiumId = stadiumId,
        stadiumName = stadiumName,
        blockId = blockId,
        blockName = blockName,
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

fun ResponseMySeatRecordDto.ResponseReviewDto.ResponseReviewImageDto.toReviewImageResponse() =
    MySeatRecordResponse.ReviewResponse.ReviewImageResponse(
        id = id,
        url = url
    )

fun ResponseMySeatRecordDto.ResponseReviewDto.ResponseReviewKeywordDto.toReviewKeywordResponse() =
    MySeatRecordResponse.ReviewResponse.ReviewKeywordResponse(
        id = id,
        content = content,
        isPositive = isPositive
    )


fun MySeatRecordRequest.toMySeatRecordRequestDto() = RequestMySeatRecordDto(
    offset = offset,
    limit = limit,
    year = year,
    month = month
)

/** baseball team **/
fun ResponseBaseballTeamDto.toBaseballTeamResponse() = BaseballTeamResponse(
    id = id,
    name = name,
    logo = logo
)