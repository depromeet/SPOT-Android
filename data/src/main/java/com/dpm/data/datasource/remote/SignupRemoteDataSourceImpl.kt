package com.dpm.data.datasource.remote

import com.dpm.data.datasource.SignupRemoteDataSource
import com.dpm.data.model.request.signup.RequestPostSignupDto
import com.dpm.data.model.response.signup.ResponsePostSignupDto
import com.dpm.data.remote.SignupService
import javax.inject.Inject

class SignupRemoteDataSourceImpl @Inject constructor(
    private val signupService: SignupService
) : SignupRemoteDataSource {
    override suspend fun postSignup(
        requestPostSignupDto: RequestPostSignupDto,
    ): ResponsePostSignupDto {
        return signupService.postSignupMember(requestPostSignupDto)
    }

    override suspend fun getSignup(accessToken: String): ResponsePostSignupDto {
        return signupService.getSignupMember(accessToken)
    }

    override suspend fun getSignupV2(
        snsProvider: String,
        accessToken: String
    ): ResponsePostSignupDto {
        return signupService.getSignupMemberV2(snsProvider, accessToken)
    }

    override suspend fun deleteWithdraw(): Unit {
        return signupService.deleteSignupMember()
    }
}