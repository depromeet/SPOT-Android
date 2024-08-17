package com.dpm.data.datasource

import okhttp3.ResponseBody

interface WebSvgDataSource {
    suspend fun downloadFileWithDynamicUrlAsync(url: String): ResponseBody
}