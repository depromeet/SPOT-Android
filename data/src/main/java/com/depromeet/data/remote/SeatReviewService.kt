package com.depromeet.data.remote

import com.depromeet.data.model.response.seatReview.StadiumNameResponseDto
import retrofit2.http.GET

interface SeatReviewService {
    @GET("/api/v1/stadiums/name")
    suspend fun getStadiumName(): StadiumNameResponseDto
}
