package com.dpm.data.datasource.remote

import com.dpm.data.datasource.ExampleDataSource
import com.dpm.data.model.response.ExampleResponseDto
import com.dpm.data.remote.ExampleService
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
