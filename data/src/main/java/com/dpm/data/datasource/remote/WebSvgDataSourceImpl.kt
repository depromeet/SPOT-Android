package com.dpm.data.datasource.remote

import com.dpm.data.datasource.WebSvgDataSource
import com.dpm.data.remote.WebSvgApiService
import okhttp3.ResponseBody
import javax.inject.Inject

class WebSvgDataSourceImpl @Inject constructor(
    private val webSvgApiService: WebSvgApiService
) : WebSvgDataSource {
    override suspend fun downloadFileWithDynamicUrlAsync(url: String): ResponseBody {
        return webSvgApiService.downloadFileWithDynamicUrlAsync(url)
    }
}