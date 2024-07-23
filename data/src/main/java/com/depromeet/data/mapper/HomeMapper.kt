package com.depromeet.data.mapper

import com.depromeet.data.model.request.home.RequestMySeatRecordDto
import com.depromeet.data.model.response.home.ResponseBaseballTeamDto
import com.depromeet.data.model.response.home.ResponsePresignedUrlDto
import com.depromeet.domain.entity.request.home.MySeatRecordRequest
import com.depromeet.domain.entity.response.home.BaseballTeamResponse
import com.depromeet.domain.entity.response.home.PresignedUrlResponse


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

/** PresignedUrl Mapper **/
fun ResponsePresignedUrlDto.toPresignedUrlResponse() = PresignedUrlResponse(
    presignedUrl = presignedUrl
)
