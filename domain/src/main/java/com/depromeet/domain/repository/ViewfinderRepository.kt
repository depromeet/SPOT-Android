package com.depromeet.domain.repository

import com.depromeet.domain.entity.request.viewfinder.RequestBlockReviewQuery
import com.depromeet.domain.entity.response.viewfinder.ResponseBlockReview
import com.depromeet.domain.entity.response.viewfinder.ResponseBlockRow
import com.depromeet.domain.entity.response.viewfinder.ResponseStadium
import com.depromeet.domain.entity.response.viewfinder.ResponseStadiums

interface ViewfinderRepository {
    suspend fun getStadiums(): Result<List<ResponseStadiums>>
    suspend fun getStadium(id : Int) : Result<ResponseStadium>
    suspend fun getBlockReviews(stadiumId:Int, blockCode: String, query: RequestBlockReviewQuery): Result<ResponseBlockReview>
    suspend fun getBlockRow(stadiumId: Int, blockCode: String): Result<ResponseBlockRow>
}