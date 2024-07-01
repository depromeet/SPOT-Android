package com.depromeet.data.datasource.remote

import com.depromeet.data.datasource.ExampleDataSource
import com.depromeet.data.model.response.ExampleResponseDto
import com.depromeet.data.remote.ExampleService
import javax.inject.Inject

class ExampleDataSourcelmpl @Inject constructor(
    private val exampleService: ExampleService,
) : ExampleDataSource {
    override suspend fun getExampleListData(
        page: Int,
    ): ExampleResponseDto {
        return exampleService.getListExample(page)
    }
}
