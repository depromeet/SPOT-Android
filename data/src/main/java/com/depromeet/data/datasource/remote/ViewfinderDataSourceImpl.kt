package com.depromeet.data.datasource.remote

import com.depromeet.data.datasource.ViewfinderDataSource
import com.depromeet.data.model.request.viewfinder.RequestBlockReviewQueryDto
import com.depromeet.data.model.response.viewfinder.ResponseBlockReviewDto
import com.depromeet.data.model.response.viewfinder.ResponseBlockRowDto
import com.depromeet.data.model.response.viewfinder.ResponseStadiumDto
import com.depromeet.data.model.response.viewfinder.ResponseStadiumsDto
import com.depromeet.data.remote.ViewfinderService
import javax.inject.Inject

class ViewfinderDataSourceImpl @Inject constructor(
    private val viewfinderService: ViewfinderService
) : ViewfinderDataSource {
    override suspend fun getStadiums(): List<ResponseStadiumsDto> {
        return viewfinderService.getStadiums()
    }

    override suspend fun getStadium(id: Int): ResponseStadiumDto {
        return viewfinderService.getStadium(id)
    }

    override suspend fun getBlockReviews(
        stadiumId: Int,
        blockCode: String,
        queryParam: RequestBlockReviewQueryDto
    ): ResponseBlockReviewDto {
        return viewfinderService.getBlockReviews(
            stadiumId,
            blockCode,
            queryParam.rowNumber,
            queryParam.seatNumber,
            queryParam.year,
            queryParam.month,
            queryParam.page,
            queryParam.size
        )
    }

    override suspend fun getBlockRow(stadiumId: Int, blockCode: String): ResponseBlockRowDto {
        return viewfinderService.getBlockRow(stadiumId, blockCode)
    }
}