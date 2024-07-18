package com.depromeet.data.repository

import com.depromeet.data.datasource.SeatReviewDataSource
import com.depromeet.data.model.request.toRequestUploadUrl
import com.depromeet.data.model.request.toSeatReview
import com.depromeet.domain.entity.request.RequestUploadUrlModel
import com.depromeet.domain.entity.request.SeatReviewModel
import com.depromeet.domain.entity.response.seatReview.RecommendRequestModel
import com.depromeet.domain.entity.response.seatReview.SeatBlockModel
import com.depromeet.domain.entity.response.seatReview.SeatRangeModel
import com.depromeet.domain.entity.response.seatReview.StadiumNameModel
import com.depromeet.domain.entity.response.seatReview.StadiumSectionModel
import com.depromeet.domain.repository.SeatReviewRepository
import javax.inject.Inject

class SeatReviewRepositoryImpl @Inject constructor(
    private val seatReviewDataSource: SeatReviewDataSource,
) : SeatReviewRepository {

    override suspend fun getStadiumName(): Result<List<StadiumNameModel>> {
        return runCatching {
            val response = seatReviewDataSource.getStadiumNameData()
            response.map { it.toStadiumName() }
        }
    }

    override suspend fun getStadiumSection(
        stadiumId: Int,
    ): Result<StadiumSectionModel?> {
        return runCatching {
            seatReviewDataSource.getStadiumSectionData(
                stadiumId,
            ).toStadiumSection()
        }
    }

    override suspend fun getSeatBlock(
        stadiumId: Int,
        sectionId: Int,
    ): Result<List<SeatBlockModel>> {
        return runCatching {
            val response = seatReviewDataSource.getSeatBlockData(stadiumId, sectionId)
            response.map { it.toSeatBlock() }
        }
    }

    override suspend fun getSeatRange(
        stadiumId: Int,
        sectionId: Int,
    ): Result<List<SeatRangeModel>> {
        return runCatching {
            val response = seatReviewDataSource.getSeatRangeData(stadiumId, sectionId)
            response.map { it.toSeatRange() }
        }
    }

    override suspend fun postSeatReview(
        seatReviewInfo: SeatReviewModel,
    ): Result<Unit> {
        return runCatching {
            seatReviewDataSource.postSeatReviewData(
                seatReviewInfo.toSeatReview(),
            )
        }
    }

    override suspend fun postUploadUrl(
        memberId: Int,
        requestUploadUrlModel: RequestUploadUrlModel,
    ): Result<RecommendRequestModel> {
        return runCatching {
            seatReviewDataSource.postUploadUrlData(
                memberId,
                requestUploadUrlModel.toRequestUploadUrl(),
            ).toResponseUploadUrl()
        }
    }
}
