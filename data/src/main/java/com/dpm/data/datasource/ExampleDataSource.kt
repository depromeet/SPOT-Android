package com.dpm.data.datasource

import com.dpm.data.model.response.ExampleResponseDto

interface ExampleDataSource {
    suspend fun getExampleListData(
        page: Int,
    ): ExampleResponseDto
}
