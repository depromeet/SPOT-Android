package com.depromeet.data.repository

import com.depromeet.data.datasource.HomeDataSource
import com.depromeet.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource,
) : HomeRepository {
}