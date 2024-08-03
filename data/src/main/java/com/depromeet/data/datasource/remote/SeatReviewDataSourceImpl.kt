package com.depromeet.data.datasource.remote

import com.depromeet.data.datasource.SeatReviewDataSource
import com.depromeet.data.model.request.seatReview.RequestPreSignedUrlDto
import com.depromeet.data.model.request.seatReview.RequestSeatReviewDto
import com.depromeet.data.model.response.seatReview.ResponsePreSignedUrlDto
import com.depromeet.data.model.response.seatReview.ResponseSeatBlockDto
import com.depromeet.data.model.response.seatReview.ResponseSeatRangeDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumNameDto
import com.depromeet.data.model.response.seatReview.ResponseStadiumSectionDto
import com.depromeet.data.remote.SeatReviewService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class SeatReviewDataSourceImpl @Inject constructor(
    private val seatReviewService: SeatReviewService,
) : SeatReviewDataSource {
    override suspend fun getStadiumNameData(): List<ResponseStadiumNameDto> {
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
    ): List<ResponseSeatBlockDto> {
        return seatReviewService.getSeatBlock(stadiumId, sectionId)
    }

    override suspend fun getSeatRangeData(
        stadiumId: Int,
        sectionId: Int,
    ): List<ResponseSeatRangeDto> {
        return seatReviewService.getSeatRange(stadiumId, sectionId)
    }

    override suspend fun postImagePreSignedData(
        fileExtension: String,
    ): ResponsePreSignedUrlDto {
        return seatReviewService.postImagePreSignedUrl(
            RequestPreSignedUrlDto(fileExtension),
        )
    }

    override suspend fun putReviewImageData(
        presignedUrl: String,
        image: ByteArray,
    ) {
        val mediaType = "image/*".toMediaTypeOrNull()
        return seatReviewService.putProfileImage(presignedUrl, image.toRequestBody(mediaType))
    }

    override suspend fun postSeatReviewData(
        blockId: Int,
        seatNumber: Int,
        requestSeatReviewDto: RequestSeatReviewDto,
    ) {
        return seatReviewService.postSeatReview(
            blockId,
            seatNumber,
            requestSeatReviewDto,
        )
    }
}
