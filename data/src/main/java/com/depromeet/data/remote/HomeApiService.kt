package com.depromeet.data.remote

import com.depromeet.data.model.request.home.RequestFileExtensionDto
import com.depromeet.data.model.request.home.RequestProfileEditDto
import com.depromeet.data.model.response.home.ResponseBaseballTeamDto
import com.depromeet.data.model.response.home.ResponseDeleteReviewDto
import com.depromeet.data.model.response.home.ResponseHomeFeedDto
import com.depromeet.data.model.response.home.ResponseLevelByPostDto
import com.depromeet.data.model.response.home.ResponseMySeatRecordDto
import com.depromeet.data.model.response.home.ResponsePresignedUrlDto
import com.depromeet.data.model.response.home.ResponseProfileDto
import com.depromeet.data.model.response.home.ResponseProfileEditDto
import com.depromeet.data.model.response.home.ResponseRecentReviewDto
import com.depromeet.data.model.response.home.ResponseReviewDateDto
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface HomeApiService {
    @GET("/api/v1/reviews")
    suspend fun getMySeatRecord(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("year") year: Int?,
        @Query("month") month: Int?,
    ): ResponseMySeatRecordDto

    @GET("/api/v1/baseball-teams")
    suspend fun getBaseballTeam(): List<ResponseBaseballTeamDto>

    @POST("/api/v1/members/profile/images")
    suspend fun postProfileImagePresigned(
        @Body body: RequestFileExtensionDto,
    ): ResponsePresignedUrlDto

    @PUT
    suspend fun putProfileImage(
        @Url preSignedUrl: String,
        @Body image: RequestBody,
    )

    @PUT("/api/v1/members")
    suspend fun putProfileEdit(
        @Body body: RequestProfileEditDto,
    ): ResponseProfileEditDto

    @GET("/api/v1/members/duplicatedNickname/{nickname}")
    suspend fun getDuplicateNickname(
        @Path("nickname") nickname: String,
    )

    @GET("/api/v1/reviews/months")
    suspend fun getReviewDate(): ResponseReviewDateDto

    @GET("/api/v1/members/memberInfo")
    suspend fun getProfileInfo(): ResponseProfileDto

    @GET("/api/v1/reviews/recentReview")
    suspend fun getRecentReview(): ResponseRecentReviewDto

    @DELETE("/api/v1/reviews/{reviewId}")
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Int,
    ): ResponseDeleteReviewDto

    @GET("/api/v1/levelUpConditions")
    suspend fun getLevelByPost() : List<ResponseLevelByPostDto>

    @GET("/api/v1/members/homeFeed")
    suspend fun getHomeFeed() : ResponseHomeFeedDto
}