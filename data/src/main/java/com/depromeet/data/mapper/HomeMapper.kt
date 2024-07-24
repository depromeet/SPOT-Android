package com.depromeet.data.mapper

import com.depromeet.data.model.response.home.ResponseBaseballTeamDto
import com.depromeet.data.model.response.home.ResponsePresignedUrlDto
import com.depromeet.domain.entity.response.home.BaseballTeamResponse
import com.depromeet.domain.entity.response.home.PresignedUrlResponse


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
