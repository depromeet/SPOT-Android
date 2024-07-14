package com.depromeet.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface WebSvgApiService {
    @Streaming
    @GET
    suspend fun downloadFileWithDynamicUrlAsync(
        @Url fileUrl: String
    ): ResponseBody
}