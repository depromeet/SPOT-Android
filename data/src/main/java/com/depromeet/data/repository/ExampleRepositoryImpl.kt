package com.depromeet.data.repository

import com.depromeet.data.datasource.ExampleDataSource
import com.depromeet.domain.entity.response.ExampleListModel
import com.depromeet.domain.repository.ExampleRepository
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
