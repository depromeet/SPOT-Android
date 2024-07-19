package com.depromeet.data.remote

import com.depromeet.data.model.response.viewfinder.BlockReviewResponseDto
import com.depromeet.data.model.response.viewfinder.StadiumResponseDto
import com.depromeet.data.model.response.viewfinder.StadiumsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ViewfinderService {
    @GET("/api/v1/stadiums")
    suspend fun getStadiums(): List<StadiumsResponseDto>

    @GET("/api/v1/stadiums/{stadiumId}")
    suspend fun getStadium(
        @Path("stadiumId") stadiumId: Int
    ): StadiumResponseDto

    @GET("/api/v1/stadiums/{stadiumId}/blocks/{blockId}/reviews")
    suspend fun getBlockReviews(
        @Path("stadiumId") stadiumId: Int,
        @Path("blockId") blockId: String
    ): BlockReviewResponseDto
}