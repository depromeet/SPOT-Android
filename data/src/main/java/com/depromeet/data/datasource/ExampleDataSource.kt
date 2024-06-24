package com.depromeet.data.datasource

import com.depromeet.data.model.response.ExampleResponseDto

interface ExampleDataSource {
    suspend fun getExampleListData(
        page: Int,
    ): ExampleResponseDto
}
