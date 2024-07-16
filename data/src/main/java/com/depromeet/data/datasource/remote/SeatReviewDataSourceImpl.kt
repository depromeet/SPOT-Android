package com.depromeet.data.datasource.remote

import com.depromeet.data.datasource.SeatReviewDataSource
import com.depromeet.data.model.request.RequestSeatReviewDto
import com.depromeet.data.model.response.seatReview.ResponseSeatBlockDto
import com.depromeet.data.model.response.seatReview.ResponseSeatMaxDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumNameDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumSectionDto
import com.depromeet.data.remote.SeatReviewService
import javax.inject.Inject

class SeatReviewDataSourceImpl @Inject constructor(
    private val seatReviewService: SeatReviewService,
) : SeatReviewDataSource {
    override suspend fun getStadiumNameData(): ResponseStadiumNameDto {
        return seatReviewService.getStadiumName()
    }

    override suspend fun getStadiumSectionData(
        stadiumId: Int,
    ): ResponseStadiumSectionDto {
        return seatReviewService.getStadiumSection(stadiumId)
    }

    override suspend fun getSeatBlockData(
        stadiumId: Int,
        sectionId: Int,
    ): ResponseSeatBlockDto {
        return seatReviewService.getSeatBlock(stadiumId, sectionId)
    }

    override suspend fun getSeatMaxData(
        stadiumId: Int,
        sectionId: Int,
    ): ResponseSeatMaxDto {
        return seatReviewService.getSeatMax(stadiumId, sectionId)
    }

    override suspend fun postSeatReviewData(requestSeatReviewDto: RequestSeatReviewDto) {
        return seatReviewService.postSeatReview(requestSeatReviewDto)
    }
}
