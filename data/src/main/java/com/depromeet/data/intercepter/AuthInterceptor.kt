package com.depromeet.data.intercepter

import com.depromeet.data.BuildConfig.S3_URL
import com.depromeet.domain.preference.SharedPreference
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sharedPreference: SharedPreference,
) : Interceptor {
    private val encodedToken: String
        get() = "Bearer ${sharedPreference.token}"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (!shouldRequestAuthenticatedHeaders(originalRequest.url.encodedPath) || urlIsS3(
                originalRequest.url.toString()
            )
        ) {
            return chain.proceed(originalRequest)
        }

        var response = proceedWithAuthorizationHeader(chain, originalRequest, encodedToken)
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            runBlocking {
                val isSuccess = tryRefreshToken()
                if (isSuccess) {
                    response.close()
                    response = proceedWithAuthorizationHeader(chain, originalRequest, encodedToken)
                } else {
                    sharedPreference.clear()
                }
            }
        }
        return response
    }

    private fun urlIsS3(url: String): Boolean {
        return url.contains(S3_URL)
    }


    private fun shouldRequestAuthenticatedHeaders(encodedPath: String) = when (encodedPath) {
        "/api/v1/baseball-teams" -> false
        "/api/v1/members" -> true // TODO : 프로필 수정과 URL이 겹쳐서 수정했습니다.
        "/api/v1/members/{accessToken}" -> false //TODO 추 후 변경 필요
        else -> true
    }

    private fun proceedWithAuthorizationHeader(
        chain: Interceptor.Chain,
        request: Request,
        token: String,
    ): Response {
        val newRequest = request.newBuilder()
            .addHeader("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }

    private suspend fun tryRefreshToken(): Boolean {
        // TODO : 리프레시 토큰 발급 로직
        return true
    }
}

