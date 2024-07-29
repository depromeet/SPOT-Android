package com.depromeet.domain.repository

import com.depromeet.domain.entity.request.viewfinder.BlockReviewRequestQuery
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.domain.entity.response.viewfinder.BlockRowResponse
import com.depromeet.domain.entity.response.viewfinder.StadiumResponse
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse

interface ViewfinderRepository {
    suspend fun getStadiums(): Result<List<StadiumsResponse>>
    suspend fun getStadium(id : Int) : Result<StadiumResponse>
    suspend fun getBlockReviews(stadiumId:Int, blockCode: String, query: BlockReviewRequestQuery): Result<BlockReviewResponse>
    suspend fun getBlockRow(stadiumId: Int, blockCode: String): Result<BlockRowResponse>
}