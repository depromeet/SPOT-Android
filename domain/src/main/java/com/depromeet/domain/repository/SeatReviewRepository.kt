package com.depromeet.domain.repository

import com.depromeet.domain.entity.response.ExampleListModel

interface SeatReviewRepository {
    suspend fun getListExample(
        page: Int,
    ): Result<ExampleListModel?>
}
