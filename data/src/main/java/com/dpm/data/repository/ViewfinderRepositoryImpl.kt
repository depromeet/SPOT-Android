package com.dpm.data.repository

import com.dpm.data.datasource.ViewfinderDataSource
import com.dpm.data.model.request.viewfinder.toBlockReviewRequestQueryDto
import com.dpm.data.model.response.viewfinder.toBlockReviewResponse
import com.dpm.data.model.response.viewfinder.toBlockRowResponse
import com.dpm.data.model.response.viewfinder.toStadiumResponse
import com.dpm.data.model.response.viewfinder.toStadiumsResponse
import com.dpm.domain.entity.request.viewfinder.RequestBlockReviewQuery
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview
import com.dpm.domain.entity.response.viewfinder.ResponseBlockRow
import com.dpm.domain.entity.response.viewfinder.ResponseStadium
import com.dpm.domain.entity.response.viewfinder.ResponseStadiums
import com.dpm.domain.repository.ViewfinderRepository
import javax.inject.Inject

class ViewfinderRepositoryImpl @Inject constructor(
    private val viewfinderDataSource: ViewfinderDataSource
) : ViewfinderRepository {
    override suspend fun getStadiums(): Result<List<ResponseStadiums>> {
        return runCatching {
            viewfinderDataSource.getStadiums().map { it.toStadiumsResponse() }
        }
    }

    override suspend fun getStadium(id: Int): Result<ResponseStadium> {
        return runCatching {
            viewfinderDataSource.getStadium(id).toStadiumResponse()
        }
    }

    override suspend fun getBlockReviews(
        stadiumId: Int,
        blockCode: String,
        query: RequestBlockReviewQuery
    ): Result<ResponseBlockReview> {
        return runCatching {
            viewfinderDataSource.getBlockReviews(
                stadiumId,
                blockCode,
                query.toBlockReviewRequestQueryDto()
            ).toBlockReviewResponse()
        }
    }

    override suspend fun getBlockRow(stadiumId: Int, blockCode: String): Result<ResponseBlockRow> {
        return runCatching {
            viewfinderDataSource.getBlockRow(stadiumId, blockCode).toBlockRowResponse()
        }
    }
}