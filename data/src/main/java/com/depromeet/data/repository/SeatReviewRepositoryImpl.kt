package com.depromeet.data.repository

import com.depromeet.data.datasource.SeatReviewDataSource
import com.depromeet.data.model.request.seatreview.toSeatReview
import com.depromeet.domain.entity.request.seatreview.RequestSeatReview
import com.depromeet.domain.entity.response.seatreview.ResponsePresignedUrl
import com.depromeet.domain.entity.response.seatreview.ResponseSeatBlock
import com.depromeet.domain.entity.response.seatreview.ResponseSeatRange
import com.depromeet.domain.entity.response.seatreview.ResponseStadiumName
import com.depromeet.domain.entity.response.seatreview.ResponseStadiumSection
import com.depromeet.domain.repository.SeatReviewRepository
import javax.inject.Inject

class SeatReviewRepositoryImpl @Inject constructor(
    private val seatReviewDataSource: SeatReviewDataSource,
) : SeatReviewRepository {

    override suspend fun getStadiumName(): Result<List<ResponseStadiumName>> {
        return runCatching {
            val response = seatReviewDataSource.getStadiumNameData()
            response.map { it.toStadiumName() }
        }
    }

    override suspend fun getStadiumSection(
        stadiumId: Int,
    ): Result<ResponseStadiumSection?> {
        return runCatching {
            seatReviewDataSource.getStadiumSectionData(
                stadiumId,
            ).toStadiumSection()
        }
    }

    override suspend fun getSeatBlock(
        stadiumId: Int,
        sectionId: Int,
    ): Result<List<ResponseSeatBlock>> {
        return runCatching {
            val response = seatReviewDataSource.getSeatBlockData(stadiumId, sectionId)
            response.map { it.toSeatBlock() }
        }
    }

    override suspend fun getSeatRange(
        stadiumId: Int,
        sectionId: Int,
    ): Result<List<ResponseSeatRange>> {
        return runCatching {
            val response = seatReviewDataSource.getSeatRangeData(stadiumId, sectionId)
            response.map { it.toSeatRange() }
        }
    }

    override suspend fun postReviewImagePresigned(
        fileExtension: String,
    ): Result<ResponsePresignedUrl> {
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
        seatReviewInfo: RequestSeatReview,
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
