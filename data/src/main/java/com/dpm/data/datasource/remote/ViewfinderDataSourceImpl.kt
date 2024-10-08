package com.dpm.data.datasource.remote

import com.dpm.data.datasource.ViewfinderDataSource
import com.dpm.data.model.request.viewfinder.RequestBlockReviewQueryDto
import com.dpm.data.model.response.viewfinder.ResponseBlockReviewDto
import com.dpm.data.model.response.viewfinder.ResponseBlockRowDto
import com.dpm.data.model.response.viewfinder.ResponseStadiumDto
import com.dpm.data.model.response.viewfinder.ResponseStadiumsDto
import com.dpm.data.remote.ViewfinderService
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
            queryParam.cursor,
            queryParam.sortBy,
            queryParam.size
        )
    }

    override suspend fun getBlockRow(stadiumId: Int, blockCode: String): ResponseBlockRowDto {
        return viewfinderService.getBlockRow(stadiumId, blockCode)
    }

    override suspend fun updateScrap(reviewId: Int) {
        viewfinderService.postScrap(reviewId)
    }

    override suspend fun updateLike(reviewId: Int) {
        viewfinderService.postLike(reviewId)
    }
}