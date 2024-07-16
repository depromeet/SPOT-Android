package com.depromeet.data.repository

import com.depromeet.data.datasource.SeatReviewDataSource
import com.depromeet.data.model.request.toSeatReview
import com.depromeet.domain.entity.request.SeatReviewModel
import com.depromeet.domain.entity.response.seatReview.SeatBlockModel
import com.depromeet.domain.entity.response.seatReview.SeatMaxModel
import com.depromeet.domain.entity.response.seatReview.StadiumNameModel
import com.depromeet.domain.entity.response.seatReview.StadiumSectionModel
import com.depromeet.domain.repository.SeatReviewRepository
import javax.inject.Inject

class SeatReviewRepositoryImpl @Inject constructor(
    private val seatReviewDataSource: SeatReviewDataSource,
) : SeatReviewRepository {

    override suspend fun getStadiumName(): Result<StadiumNameModel?> {
        return runCatching {
            seatReviewDataSource.getStadiumNameData().toStadiumName()
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
    ): Result<SeatBlockModel?> {
        return runCatching {
            seatReviewDataSource.getSeatBlockData(
                stadiumId,
                sectionId,
            ).toSeatBlock()
        }
    }

    override suspend fun getSeatMax(
        stadiumId: Int,
        sectionId: Int,
    ): Result<SeatMaxModel?> {
        return runCatching {
            seatReviewDataSource.getSeatMaxData(
                stadiumId,
                sectionId,
            ).toSeatMax()
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
}
