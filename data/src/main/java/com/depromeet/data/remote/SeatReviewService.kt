package com.depromeet.data.remote

import com.depromeet.data.model.request.RequestSeatReviewDto
import com.depromeet.data.model.response.seatReview.ResponseSeatBlockDto
import com.depromeet.data.model.response.seatReview.ResponseSeatMaxDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumNameDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumSectionDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SeatReviewService {
    @GET("/api/v1/stadiums/names")
    suspend fun getStadiumName(): List<ResponseStadiumNameDto>

    @GET("/api/v1/stadiums/{stadiumId}/sections")
    suspend fun getStadiumSection(
        @Path("stadiumId") stadiumId: Int,
    ): ResponseStadiumSectionDto

    @GET("/api/v1/stadiums/{stadiumId}/sections/{sectionId}/blocks")
    suspend fun getSeatBlock(
        @Path("stadiumId") stadiumId: Int,
        @Path("sectionId") sectionId: Int,
    ): List<ResponseSeatBlockDto>

    @GET("/api/v1/stadiums/{stadiumId}/sections/{sectionId}/blocks/rows")
    suspend fun getSeatMax(
        @Path("stadiumId") stadiumId: Int,
        @Path("sectionId") sectionId: Int,
    ): ResponseSeatMaxDto

    @POST("/api/v1/reviews")
    suspend fun postSeatReview(
        @Body requestPostSignupDto: RequestSeatReviewDto,
    )
}
