package com.depromeet.data.datasource.remote

import com.depromeet.data.datasource.ViewfinderDataSource
import com.depromeet.data.model.request.viewfinder.BlockReviewRequestQueryDto
import com.depromeet.data.model.response.viewfinder.BlockReviewResponseDto
import com.depromeet.data.model.response.viewfinder.BlockRowResponseDto
import com.depromeet.data.model.response.viewfinder.StadiumResponseDto
import com.depromeet.data.model.response.viewfinder.StadiumsResponseDto
import com.depromeet.data.remote.ViewfinderService
import javax.inject.Inject

class ViewfinderDataSourceImpl @Inject constructor(
    private val viewfinderService: ViewfinderService
) : ViewfinderDataSource {
    override suspend fun getStadiums(): List<StadiumsResponseDto> {
        return viewfinderService.getStadiums()
    }

    override suspend fun getStadium(id: Int): StadiumResponseDto {
        return viewfinderService.getStadium(id)
    }

    override suspend fun getBlockReviews(
        stadiumId: Int,
        blockCode: String,
        queryParam: BlockReviewRequestQueryDto
    ): BlockReviewResponseDto {
        return viewfinderService.getBlockReviews(
            stadiumId,
            blockCode,
            queryParam.rowId,
            queryParam.seatNumber,
            queryParam.offset,
            queryParam.limit
        )
    }

    override suspend fun getBlockRow(stadiumId: Int, blockCode: String): BlockRowResponseDto {
        return viewfinderService.getBlockRow(stadiumId, blockCode)
    }
}