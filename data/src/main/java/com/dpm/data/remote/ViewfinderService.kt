package com.dpm.data.remote

import com.dpm.data.model.response.viewfinder.ResponseBlockReviewDto
import com.dpm.data.model.response.viewfinder.ResponseBlockRowDto
import com.dpm.data.model.response.viewfinder.ResponseStadiumDto
import com.dpm.data.model.response.viewfinder.ResponseStadiumsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ViewfinderService {
    @GET("/api/v1/stadiums")
    suspend fun getStadiums(): List<ResponseStadiumsDto>

    @GET("/api/v1/stadiums/{stadiumId}")
    suspend fun getStadium(
        @Path("stadiumId") stadiumId: Int
    ): ResponseStadiumDto

    @GET("/api/v1/stadiums/{stadiumId}/blocks/{blockCode}/reviews")
    suspend fun getBlockReviews(
        @Path("stadiumId") stadiumId: Int,
        @Path("blockCode") blockCode: String,
        @Query("rowNumber") rowNumber: Int?,
        @Query("seatNumber") seatNumber: Int?,
        @Query("year") year: Int?,
        @Query("month") month: Int?,
        @Query("cursor") cursor: String?,
        @Query("sortBy") sortBy: String?,
        @Query("size") size: Int?,
    ): ResponseBlockReviewDto

    @GET("/api/v1/stadiums/{stadiumId}/blocks/{blockCode}/rows")
    suspend fun getBlockRow(
        @Path("stadiumId") stadiumId: Int,
        @Path("blockCode") blockCode: String
    ): ResponseBlockRowDto
}