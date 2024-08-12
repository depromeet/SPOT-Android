package com.depromeet.data.mapper

import com.depromeet.data.model.response.home.ResponseBaseballTeamDto
import com.depromeet.data.model.response.home.ResponsePresignedUrlDto
import com.depromeet.domain.entity.response.home.ResponseBaseballTeam
import com.depromeet.domain.entity.response.home.ResponsePresignedUrl


/** baseball team **/
fun ResponseBaseballTeamDto.toBaseballTeamResponse() = ResponseBaseballTeam(
    id = id,
    name = name,
    logo = logo
)

/** PresignedUrl Mapper **/
fun ResponsePresignedUrlDto.toPresignedUrlResponse() = ResponsePresignedUrl(
    presignedUrl = presignedUrl
)
