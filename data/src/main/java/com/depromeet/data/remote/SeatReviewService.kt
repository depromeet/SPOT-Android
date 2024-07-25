package com.depromeet.data.remote

import com.depromeet.data.model.request.RequestPreSignedUrlDto
import com.depromeet.data.model.request.RequestSeatReviewDto
import com.depromeet.data.model.response.seatReview.ResponsePreSignedUrlDto
import com.depromeet.data.model.response.seatReview.ResponseSeatBlockDto
import com.depromeet.data.model.response.seatReview.ResponseSeatRangeDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumNameDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumSectionDto
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

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
    suspend fun getSeatRange(
        @Path("stadiumId") stadiumId: Int,
        @Path("sectionId") sectionId: Int,
    ): List<ResponseSeatRangeDto>

    @POST("/api/v1/members/{memberId}/reviews/images")
    suspend fun postImagePreSignedUrl(
        @Body body: RequestPreSignedUrlDto,
        @Path("memberId") memberId: Int,
    ): ResponsePreSignedUrlDto

    @PUT
    suspend fun putProfileImage(
        @Url preSignedUrl: String,
        @Body image: RequestBody,
    )

    @POST("/api/v1/seats/{seatId}/members/{memberId}/reviews")
    suspend fun postSeatReview(
        @Path("blockId") blockId: Int,
        @Path("seatNumber") seatNumber: Int,
        @Body requestPostSignupDto: RequestSeatReviewDto,
    )
}
