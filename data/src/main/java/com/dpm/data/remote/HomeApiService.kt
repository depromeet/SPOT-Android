package com.dpm.data.remote

import com.dpm.data.model.request.home.RequestFileExtensionDto
import com.dpm.data.model.request.home.RequestProfileEditDto
import com.dpm.data.model.response.home.ResponseBaseballTeamDto
import com.dpm.data.model.response.home.ResponseDeleteReviewDto
import com.dpm.data.model.response.home.ResponseHomeFeedDto
import com.dpm.data.model.response.home.ResponseLevelByPostDto
import com.dpm.data.model.response.home.ResponseLevelUpInfoDto
import com.dpm.data.model.response.home.ResponseMySeatRecordDto
import com.dpm.data.model.response.home.ResponsePresignedUrlDto
import com.dpm.data.model.response.home.ResponseProfileDto
import com.dpm.data.model.response.home.ResponseProfileEditDto
import com.dpm.data.model.response.home.ResponseRecentReviewDto
import com.dpm.data.model.response.home.ResponseReviewDateDto
import com.dpm.data.model.response.home.ResponseScrapDto
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
        @Query("cursor") cursor: String?,
        @Query("sortBy") sortBy: String,
        @Query("size") size: Int,
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

    @GET("/api/v1/levels/info")
    suspend fun getLevelByPost(): List<ResponseLevelByPostDto>

    @GET("/api/v1/members/homeFeed")
    suspend fun getHomeFeed(): ResponseHomeFeedDto

    @GET("/api/v1/levels/up/info")
    suspend fun getLevelUpInfo(
        @Query("nextLevel") nextLevel: Int,
    ): ResponseLevelUpInfoDto

    @GET("api/v1/reviews/scraps")
    suspend fun getScrap(
        @Query("cursor") cursor: String?,
        @Query("size") size: Int,
        @Query("sortBy") sortBy: String,
        @Query("stadiumId") stadiumId: Int?,
        @Query("months") months: List<Int>?,
        @Query("good") good: List<String>?,
        @Query("bad") bad: List<String>?,
    ): ResponseScrapDto
}