package com.depromeet.data.mapper

import com.depromeet.data.model.response.StadiumResponseDto
import com.depromeet.data.model.response.StadiumsResponseDto
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