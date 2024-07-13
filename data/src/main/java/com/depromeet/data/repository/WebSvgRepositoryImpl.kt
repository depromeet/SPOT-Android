package com.depromeet.data.repository

import com.depromeet.data.datasource.remote.WebSvgDataSourceImpl
import com.depromeet.domain.repository.WebSvgRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WebSvgRepositoryImpl @Inject constructor(
    private val webSvgDataSourceImpl: WebSvgDataSourceImpl
) : WebSvgRepository {
    override fun downloadFileWithDynamicUrlAsync(url: String): Flow<String> = flow {
        try {
            val responseBody = webSvgDataSourceImpl.downloadFileWithDynamicUrlAsync(url)
            emit(responseBody.string())
        } catch (e: Exception) {
            emit("")
        }
    }
}