package com.dpm.data.datasource

import com.dpm.data.model.request.viewfinder.RequestBlockReviewQueryDto
import com.dpm.data.model.response.viewfinder.ResponseBlockReviewDto
import com.dpm.data.model.response.viewfinder.ResponseBlockRowDto
import com.dpm.data.model.response.viewfinder.ResponseStadiumDto
import com.dpm.data.model.response.viewfinder.ResponseStadiumsDto

interface ViewfinderDataSource {
    suspend fun getStadiums(): List<ResponseStadiumsDto>
    suspend fun getStadium(id: Int): ResponseStadiumDto
    suspend fun getBlockReviews(
        stadiumId: Int,
        blockCode: String,
        queryParam: RequestBlockReviewQueryDto
    ): ResponseBlockReviewDto
    suspend fun getBlockRow(stadiumId: Int, blockCode: String): ResponseBlockRowDto
    suspend fun updateScrap(reviewId: Int)
    suspend fun updateLike(reviewId: Int)
}