package com.depromeet.data.intercepter

import com.depromeet.domain.preference.SharedPreference
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
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
        if (!shouldRequestAuthenticatedHeaders(originalRequest.url.encodedPath)) {
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

    private fun shouldRequestAuthenticatedHeaders(encodedPath: String) = when (encodedPath) {
        "/api/v1/members" -> false
        "/api/v1/members/{accessToken}" -> false //TODO 추 후 변경 필요
        else -> true
    }

    private fun proceedWithAuthorizationHeader(chain: Interceptor.Chain, request: Request, token: String): Response {
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

