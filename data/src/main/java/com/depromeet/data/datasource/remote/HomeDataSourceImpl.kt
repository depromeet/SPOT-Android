package com.depromeet.data.datasource.remote

import com.depromeet.data.datasource.HomeDataSource
import com.depromeet.data.remote.HomeApiService
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(
    private val homeApiService: HomeApiService
) : HomeDataSource {
}