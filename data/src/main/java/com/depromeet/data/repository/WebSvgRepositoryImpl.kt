package com.depromeet.data.repository

import com.depromeet.data.datasource.remote.WebSvgDataSourceImpl
import com.depromeet.domain.repository.WebSvgRepository
import javax.inject.Inject

class WebSvgRepositoryImpl @Inject constructor(
    private val webSvgDataSourceImpl: WebSvgDataSourceImpl
) : WebSvgRepository {
    override suspend fun downloadFileWithDynamicUrlAsync(url: String): String = try {
        val responseBody = webSvgDataSourceImpl.downloadFileWithDynamicUrlAsync(url)
        responseBody.string()
    } catch (e: Exception) {
        e.message.toString()
    }
}