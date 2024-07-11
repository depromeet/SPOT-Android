package com.depromeet.presentation.viewfinder.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class StadiumViewModel @Inject constructor() : ViewModel() {
    val svgString = MutableLiveData<String>("")

    fun downloadFileFromServer(url: String) {
        viewModelScope.launch {
            try {
                val responseBody = Repository.downloadFileFromServer(url)
                svgString.value = responseBody.string()
            } catch (e: Exception) {
                e.message
            }
        }
    }
}

interface APIService {
    @Streaming
    @GET
    suspend fun downloadFileWithDynamicUrlAsync(
        @Url fileUrl: String
    ): ResponseBody
}

object Repository {
    private const val APPLICATION_JSON = "application/json"
    private lateinit var apiService: APIService

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val intercept = httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY // to check the log
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(intercept)
            .build()
        val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
        val retrofit = Retrofit.Builder()
            .baseUrl("https://svgshare.com")
            .addConverterFactory(json.asConverterFactory(APPLICATION_JSON.toMediaType()))
            .client(okHttpClient)
            .build()

        apiService = retrofit.create(APIService::class.java)
    }

    suspend fun downloadFileFromServer(url: String) =
        apiService.downloadFileWithDynamicUrlAsync(url)
}