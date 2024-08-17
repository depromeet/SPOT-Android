package com.dpm.data.repository

import com.dpm.data.datasource.ExampleDataSource
import com.dpm.domain.entity.response.ExampleListModel
import com.dpm.domain.repository.ExampleRepository
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val exampleDataSource: ExampleDataSource,
) : ExampleRepository {
    override suspend fun getListExample(
        page: Int,
    ): Result<ExampleListModel?> {
        return runCatching {
            exampleDataSource.getExampleListData(
                page,
            ).toExampleEntity()
        }
    }
}
