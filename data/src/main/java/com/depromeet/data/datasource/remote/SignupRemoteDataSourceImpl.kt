package com.depromeet.data.datasource.remote

import com.depromeet.data.datasource.SignupRemoteDataSource
import com.depromeet.data.model.request.signup.RequestPostSignupDto
import com.depromeet.data.model.response.signup.ResponsePostSignupDto
import com.depromeet.data.remote.SignupService
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

    override suspend fun deleteWithdraw(): Unit {
        return signupService.deleteSignupMember()
    }
}