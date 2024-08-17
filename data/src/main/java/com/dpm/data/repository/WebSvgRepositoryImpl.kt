package com.dpm.data.repository

import com.dpm.data.datasource.remote.WebSvgDataSourceImpl
import com.dpm.domain.repository.WebSvgRepository
import javax.inject.Inject

class WebSvgRepositoryImpl @Inject constructor(
    private val webSvgDataSourceImpl: WebSvgDataSourceImpl
) : WebSvgRepository {
    override suspend fun downloadFileWithDynamicUrlAsync(url: String): Result<String> =
        runCatching {
            val responseBody = webSvgDataSourceImpl.downloadFileWithDynamicUrlAsync(url)
            responseBody.string()
        }
}