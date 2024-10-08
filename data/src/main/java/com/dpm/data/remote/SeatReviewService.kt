package com.dpm.data.remote

import com.dpm.data.model.request.seatreview.RequestPreSignedUrlDto
import com.dpm.data.model.request.seatreview.RequestSeatReviewDto
import com.dpm.data.model.response.seatreview.ResponsePreSignedUrlDto
import com.dpm.data.model.response.seatreview.ResponseSeatBlockDto
import com.dpm.data.model.response.seatreview.ResponseSeatRangeDto
import com.dpm.data.model.response.seatreview.ResponseSeatReviewDto
import com.dpm.data.model.response.seatreview.ResponseStadiumNameDto
import com.dpm.data.model.response.seatreview.ResponseStadiumSectionDto
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

    @POST("/api/v1/reviews/images")
    suspend fun postImagePreSignedUrl(
        @Body body: RequestPreSignedUrlDto,
    ): ResponsePreSignedUrlDto

    @PUT
    suspend fun putProfileImage(
        @Url preSignedUrl: String,
        @Body image: RequestBody,
    )

    @POST("/api/v1/blocks/{blockId}/reviews")
    suspend fun postSeatReview(
        @Path("blockId") blockId: Int,
        @Body requestPostSignupDto: RequestSeatReviewDto,
    ): ResponseSeatReviewDto
}
