package com.depromeet.data.remote

import com.depromeet.data.model.response.StadiumResponseDto
import com.depromeet.data.model.response.StadiumsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ViewfinderService {
    @GET("/api/v1/stadiums")
    suspend fun getStadiums(): List<StadiumsResponseDto>

    @GET("/api/v1/stadiums/{stadiumId}")
    suspend fun getStadium(
        @Path("stadiumId") stadiumId: Int
    ): StadiumResponseDto
}