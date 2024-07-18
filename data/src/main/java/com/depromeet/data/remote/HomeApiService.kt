package com.depromeet.data.remote

import com.depromeet.data.model.response.home.ResponseBaseballTeamDto
import com.depromeet.data.model.response.home.ResponseMySeatRecordDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApiService {
    @GET("/api/v1/reviews")
    suspend fun getMySeatRecord(
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int?,
        @Query("year") year: Int?,
        @Query("month") month: Int?,
    ): ResponseMySeatRecordDto

    @GET("/api/v1/baseball-teams")
    suspend fun getBaseballTeam(): List<ResponseBaseballTeamDto>
}