package com.dpm.data.datasource

import com.dpm.data.model.request.seatreview.RequestSeatReviewDto
import com.dpm.data.model.response.seatreview.ResponsePreSignedUrlDto
import com.dpm.data.model.response.seatreview.ResponseSeatBlockDto
import com.dpm.data.model.response.seatreview.ResponseSeatRangeDto
import com.dpm.data.model.response.seatreview.ResponseStadiumNameDto
import com.dpm.data.model.response.seatreview.ResponseStadiumSectionDto

interface SeatReviewDataSource {
    suspend fun getStadiumNameData(): List<ResponseStadiumNameDto>

    suspend fun getStadiumSectionData(
        stadiumId: Int,
    ): ResponseStadiumSectionDto

    suspend fun getSeatBlockData(
        stadiumId: Int,
        sectionId: Int,
    ): List<ResponseSeatBlockDto>

    suspend fun getSeatRangeData(
        stadiumId: Int,
        sectionId: Int,
    ): List<ResponseSeatRangeDto>

    suspend fun postImagePreSignedData(
        fileExtension: String,
    ): ResponsePreSignedUrlDto

    suspend fun putReviewImageData(
        presignedUrl: String,
        image: ByteArray,
    )

    suspend fun postSeatReviewData(
        blockId: Int,
        requestSeatReviewDto: RequestSeatReviewDto,
    )
}
