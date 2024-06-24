package com.depromeet.data.remote

import com.depromeet.data.model.response.ExampleResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ExampleService {
    @GET("example/api")
    suspend fun getListExample(
        @Query("page") page: Int,
    ): ExampleResponseDto
}
