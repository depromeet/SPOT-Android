package com.depromeet.data.remote

import com.depromeet.data.model.response.viewfinder.BlockReviewResponseDto
import com.depromeet.data.model.response.viewfinder.BlockRowResponseDto
import com.depromeet.data.model.response.viewfinder.StadiumResponseDto
import com.depromeet.data.model.response.viewfinder.StadiumsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ViewfinderService {
    @GET("/api/v1/stadiums")
    suspend fun getStadiums(): List<StadiumsResponseDto>

    @GET("/api/v1/stadiums/{stadiumId}")
    suspend fun getStadium(
        @Path("stadiumId") stadiumId: Int
    ): StadiumResponseDto

    @GET("/api/v1/stadiums/{stadiumId}/blocks/{blockCode}/reviews")
    suspend fun getBlockReviews(
        @Path("stadiumId") stadiumId: Int,
        @Path("blockCode") blockCode: String,
        @Query("rowNumber") rowNumber: Int?,
        @Query("seatNumber") seatNumber: Int?,
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int?,
    ): BlockReviewResponseDto

    @GET("/api/v1/stadiums/{stadiumId}/blocks/{blockCode}/rows")
    suspend fun getBlockRow(
        @Path("stadiumId") stadiumId: Int,
        @Path("blockCode") blockCode: String
    ): BlockRowResponseDto
}