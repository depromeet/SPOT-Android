package com.depromeet.data.datasource.remote

import com.depromeet.data.datasource.WebSvgDataSource
import com.depromeet.data.remote.WebSvgApiService
import okhttp3.ResponseBody
import javax.inject.Inject

class WebSvgDataSourceImpl @Inject constructor(
    private val webSvgApiService: WebSvgApiService
) : WebSvgDataSource {
    override suspend fun downloadFileWithDynamicUrlAsync(url: String): ResponseBody {
        return webSvgApiService.downloadFileWithDynamicUrlAsync(url)
    }
}