package com.dpm.data.mapper

import com.dpm.data.model.response.home.ResponseBaseballTeamDto
import com.dpm.data.model.response.home.ResponsePresignedUrlDto
import com.dpm.domain.entity.response.home.ResponseBaseballTeam
import com.dpm.domain.entity.response.home.ResponsePresignedUrl


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
