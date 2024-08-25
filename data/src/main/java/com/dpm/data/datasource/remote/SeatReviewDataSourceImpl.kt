package com.dpm.data.datasource.remote

import com.dpm.data.datasource.SeatReviewDataSource
import com.dpm.data.model.request.seatreview.RequestPreSignedUrlDto
import com.dpm.data.model.request.seatreview.RequestSeatReviewDto
import com.dpm.data.model.response.seatreview.ResponsePreSignedUrlDto
import com.dpm.data.model.response.seatreview.ResponseSeatBlockDto
import com.dpm.data.model.response.seatreview.ResponseSeatRangeDto
import com.dpm.data.model.response.seatreview.ResponseSeatReviewDto
import com.dpm.data.model.response.seatreview.ResponseStadiumNameDto
import com.dpm.data.model.response.seatreview.ResponseStadiumSectionDto
import com.dpm.data.remote.SeatReviewService
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
        requestSeatReviewDto: RequestSeatReviewDto,
    ): ResponseSeatReviewDto {
        return seatReviewService.postSeatReview(
            blockId,
            requestSeatReviewDto,
        )
    }
}
