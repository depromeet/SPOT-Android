package com.depromeet.data.remote

import com.depromeet.data.model.request.RequestFileExtensionDto
import com.depromeet.data.model.response.home.ResponseBaseballTeamDto
import com.depromeet.data.model.response.home.ResponseMySeatRecordDto
import com.depromeet.data.model.response.home.ResponsePresignedUrlDto
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

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

    @POST("/api/v1/members/{memberId}/profile/images")
    suspend fun postProfileImagePresigned(
        @Body body: RequestFileExtensionDto,
        @Path("memberId") memberId: Int,
    ): ResponsePresignedUrlDto

    @PUT
    suspend fun putProfileImage(
        @Url preSignedUrl: String,
        @Body image: RequestBody,
    )
}