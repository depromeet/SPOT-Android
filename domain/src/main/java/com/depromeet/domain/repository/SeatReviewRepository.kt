package com.depromeet.domain.repository

import com.depromeet.domain.entity.request.seatReview.RequestSeatReview
import com.depromeet.domain.entity.response.seatReview.ResponsePresignedUrl
import com.depromeet.domain.entity.response.seatReview.ResponseSeatBlock
import com.depromeet.domain.entity.response.seatReview.ResponseSeatRange
import com.depromeet.domain.entity.response.seatReview.ResponseStadiumName
import com.depromeet.domain.entity.response.seatReview.ResponseStadiumSection

interface SeatReviewRepository {
    suspend fun getStadiumName(): Result<List<ResponseStadiumName>>

    suspend fun getStadiumSection(
        stadiumId: Int,
    ): Result<ResponseStadiumSection?>

    suspend fun getSeatBlock(
        stadiumId: Int,
        sectionId: Int,
    ): Result<List<ResponseSeatBlock>>

    suspend fun getSeatRange(
        stadiumId: Int,
        sectionId: Int,
    ): Result<List<ResponseSeatRange>>

    suspend fun postReviewImagePresigned(
        fileExtension: String,
    ): Result<ResponsePresignedUrl>

    suspend fun putImagePreSignedUrl(
        presignedUrl: String,
        image: ByteArray,
    ): Result<Unit>

    suspend fun postSeatReview(
        seatId: Int,
        seatNumber: Int,
        seatReviewInfo: RequestSeatReview,
    ): Result<Unit>
}
