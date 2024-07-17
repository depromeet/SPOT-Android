package com.depromeet.data.repository

import com.depromeet.data.datasource.ViewfinderDataSource
import com.depromeet.data.mapper.toBlockReviewResponse
import com.depromeet.data.mapper.toStadiumResponse
import com.depromeet.data.mapper.toStadiumsResponse
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.domain.entity.response.viewfinder.StadiumResponse
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse
import com.depromeet.domain.repository.ViewfinderRepository
import javax.inject.Inject

class ViewfinderRepositoryImpl @Inject constructor(
    private val viewfinderDataSource: ViewfinderDataSource
) : ViewfinderRepository {
    override suspend fun getStadiums(): List<StadiumsResponse> {
        return try {
            viewfinderDataSource.getStadiums().map { it.toStadiumsResponse() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getStadium(id: Int): Result<StadiumResponse> {
        return try {
            Result.success(viewfinderDataSource.getStadium(id).toStadiumResponse())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBlockReviews(
        stadiumId: Int,
        blockId: String
    ): Result<BlockReviewResponse> {
        return try {
            Result.success(
                viewfinderDataSource.getBlockReviews(stadiumId, blockId).toBlockReviewResponse()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}