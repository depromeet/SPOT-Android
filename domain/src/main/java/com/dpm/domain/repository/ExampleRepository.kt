package com.dpm.domain.repository

import com.dpm.domain.entity.response.ExampleListModel

interface ExampleRepository {
    suspend fun getListExample(
        page: Int,
    ): Result<ExampleListModel?>
}
