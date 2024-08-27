package com.dpm.data.datasource

import com.dpm.data.model.request.signup.RequestPostSignupDto
import com.dpm.data.model.response.signup.ResponsePostSignupDto

interface SignupRemoteDataSource {
    suspend fun postSignup(
        requestPostSignupDto: RequestPostSignupDto,
    ): ResponsePostSignupDto

    suspend fun getSignup(
        accessToken: String,
    ): ResponsePostSignupDto

    suspend fun getSignupV2(
        snsProvider: String,
        accessToken: String,
    ): ResponsePostSignupDto

    suspend fun deleteWithdraw(): Unit
}