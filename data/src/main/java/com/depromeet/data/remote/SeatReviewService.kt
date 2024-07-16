package com.depromeet.data.remote

import com.depromeet.data.model.request.RequestSeatReviewDto
import com.depromeet.data.model.response.seatReview.ResponseSeatBlockDto
import com.depromeet.data.model.response.seatReview.ResponseSeatMaxDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumNameDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumSectionDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SeatReviewService {
    @GET("/api/v1/stadiums/name")
    suspend fun getStadiumName(): ResponseStadiumNameDto

    @GET("/api/v1/stadiums/{stadiumId}/sections")
    suspend fun getStadiumSection(
        @Query("stadiumId") stadiumId: Int,
    ): ResponseStadiumSectionDto

    @GET("/api/v1/stadiums/{stadiumId}/sections/{sectionId}/blocks")
    suspend fun getSeatBlock(
        @Query("stadiumId") stadiumId: Int,
        @Query("sectionId") sectionId: Int,
    ): ResponseSeatBlockDto

    @GET("/api/v1/stadiums/{stadiumId}/sections/{sectionId}/blocks/rows")
    suspend fun getSeatMax(
        @Query("stadiumId") stadiumId: Int,
        @Query("sectionId") sectionId: Int,
    ): ResponseSeatMaxDto

    @POST("/api/v1/reviews")
    suspend fun postSeatReview(
        @Body requestPostSignupDto: RequestSeatReviewDto,
    )
}
