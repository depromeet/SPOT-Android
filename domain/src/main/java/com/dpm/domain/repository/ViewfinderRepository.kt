package com.dpm.domain.repository

import com.dpm.domain.entity.request.viewfinder.RequestBlockReviewQuery
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview
import com.dpm.domain.entity.response.viewfinder.ResponseBlockRow
import com.dpm.domain.entity.response.viewfinder.ResponseStadium
import com.dpm.domain.entity.response.viewfinder.ResponseStadiums

interface ViewfinderRepository {
    suspend fun getStadiums(): Result<List<ResponseStadiums>>
    suspend fun getStadium(id : Int) : Result<ResponseStadium>
    suspend fun getBlockReviews(stadiumId:Int, blockCode: String, query: RequestBlockReviewQuery): Result<ResponseBlockReview>
    suspend fun getBlockRow(stadiumId: Int, blockCode: String): Result<ResponseBlockRow>
}