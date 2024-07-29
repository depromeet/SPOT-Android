package com.depromeet.data.repository

import com.depromeet.data.datasource.SeatReviewDataSource
import com.depromeet.data.model.request.toSeatReview
import com.depromeet.domain.entity.request.SeatReviewModel
import com.depromeet.domain.entity.response.seatReview.ResponsePresignedUrlModel
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

    override suspend fun postReviewImagePresigned(
        fileExtension: String,
    ): Result<ResponsePresignedUrlModel> {
        return runCatching {
            seatReviewDataSource.postImagePreSignedData(
                fileExtension,
            ).toResponsePreSignedUrl()
        }
    }

    override suspend fun putImagePreSignedUrl(
        presignedUrl: String,
        image: ByteArray,
    ): Result<Unit> {
        return runCatching {
            seatReviewDataSource.putReviewImageData(
                presignedUrl,
                image,
            )
        }
    }

    override suspend fun postSeatReview(
        blockId: Int,
        seatNumber: Int,
        seatReviewInfo: SeatReviewModel,
    ): Result<Unit> {
        return runCatching {
            seatReviewDataSource.postSeatReviewData(
                blockId,
                seatNumber,
                seatReviewInfo.toSeatReview(),
            )
        }
    }
}
