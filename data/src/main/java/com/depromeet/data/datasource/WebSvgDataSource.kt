package com.depromeet.data.datasource

import okhttp3.ResponseBody

interface WebSvgDataSource {
    suspend fun downloadFileWithDynamicUrlAsync(url: String): ResponseBody
}