package com.depromeet.domain.repository

import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.domain.entity.response.viewfinder.StadiumResponse
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse

interface ViewfinderRepository {
    suspend fun getStadiums(): List<StadiumsResponse>
    suspend fun getStadium(id : Int) : Result<StadiumResponse>
    suspend fun getBlockReviews(stadiumId:Int, blockId: String): Result<BlockReviewResponse>
}