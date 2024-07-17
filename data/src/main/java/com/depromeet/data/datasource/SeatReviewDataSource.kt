package com.depromeet.data.datasource

import com.depromeet.data.model.request.RequestSeatReviewDto
import com.depromeet.data.model.response.seatReview.ResponseSeatBlockDto
import com.depromeet.data.model.response.seatReview.ResponseSeatMaxDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumNameDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumSectionDto

interface SeatReviewDataSource {
    suspend fun getStadiumNameData(): ResponseStadiumNameDto

    suspend fun getStadiumSectionData(
        stadiumId: Int,
    ): ResponseStadiumSectionDto

    suspend fun getSeatBlockData(
        stadiumId: Int,
        sectionId: Int,
    ): List<ResponseSeatBlockDto>

    suspend fun getSeatMaxData(
        stadiumId: Int,
        sectionId: Int,
    ): ResponseSeatMaxDto

    suspend fun postSeatReviewData(
        requestSeatReviewDto: RequestSeatReviewDto,
    )
}
