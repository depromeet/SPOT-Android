package com.depromeet.domain.repository

import com.depromeet.domain.entity.request.RequestUploadUrlModel
import com.depromeet.domain.entity.request.SeatReviewModel
import com.depromeet.domain.entity.response.seatReview.RecommendRequestModel
import com.depromeet.domain.entity.response.seatReview.SeatBlockModel
import com.depromeet.domain.entity.response.seatReview.SeatRangeModel
import com.depromeet.domain.entity.response.seatReview.StadiumNameModel
import com.depromeet.domain.entity.response.seatReview.StadiumSectionModel

interface SeatReviewRepository {
    suspend fun getStadiumName(): Result<List<StadiumNameModel>>

    suspend fun getStadiumSection(
        stadiumId: Int,
    ): Result<StadiumSectionModel?>

    suspend fun getSeatBlock(
        stadiumId: Int,
        sectionId: Int,
    ): Result<List<SeatBlockModel>>

    suspend fun getSeatRange(
        stadiumId: Int,
        sectionId: Int,
    ): Result<List<SeatRangeModel>>

    suspend fun postSeatReview(
        seatReviewInfo: SeatReviewModel,
    ): Result<Unit>

    suspend fun postUploadUrl(
        memberId: Int,
        requestUploadUrlModel: RequestUploadUrlModel,
    ): Result<RecommendRequestModel>
}
