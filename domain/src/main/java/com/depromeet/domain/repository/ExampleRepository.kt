package com.depromeet.domain.repository

import com.depromeet.domain.entity.response.ExampleListModel

interface ExampleRepository {
    suspend fun getListExample(
        page: Int,
    ): Result<ExampleListModel?>
}
