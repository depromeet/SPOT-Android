package com.depromeet.data.datasource

import com.depromeet.data.model.request.viewfinder.RequestBlockReviewQueryDto
import com.depromeet.data.model.response.viewfinder.ResponseBlockReviewDto
import com.depromeet.data.model.response.viewfinder.ResponseBlockRowDto
import com.depromeet.data.model.response.viewfinder.ResponseStadiumDto
import com.depromeet.data.model.response.viewfinder.ResponseStadiumsDto

interface ViewfinderDataSource {
    suspend fun getStadiums(): List<ResponseStadiumsDto>
    suspend fun getStadium(id: Int): ResponseStadiumDto
    suspend fun getBlockReviews(
        stadiumId: Int,
        blockCode: String,
        queryParam: RequestBlockReviewQueryDto
    ): ResponseBlockReviewDto
    suspend fun getBlockRow(stadiumId: Int, blockCode: String): ResponseBlockRowDto
}