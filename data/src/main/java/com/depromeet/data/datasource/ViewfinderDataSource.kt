package com.depromeet.data.datasource

import com.depromeet.data.model.request.viewfinder.BlockReviewRequestQueryDto
import com.depromeet.data.model.response.viewfinder.BlockReviewResponseDto
import com.depromeet.data.model.response.viewfinder.BlockRowResponseDto
import com.depromeet.data.model.response.viewfinder.StadiumResponseDto
import com.depromeet.data.model.response.viewfinder.StadiumsResponseDto

interface ViewfinderDataSource {
    suspend fun getStadiums(): List<StadiumsResponseDto>
    suspend fun getStadium(id: Int): StadiumResponseDto
    suspend fun getBlockReviews(
        stadiumId: Int,
        blockCode: String,
        queryParam: BlockReviewRequestQueryDto
    ): BlockReviewResponseDto
    suspend fun getBlockRow(stadiumId: Int, blockCode: String): BlockRowResponseDto
}