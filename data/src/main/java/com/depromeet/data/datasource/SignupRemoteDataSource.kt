package com.depromeet.data.datasource

import com.depromeet.data.model.request.signup.RequestPostSignupDto
import com.depromeet.data.model.response.signup.ResponsePostSignupDto

interface SignupRemoteDataSource {
    suspend fun postSignup(
        requestPostSignupDto: RequestPostSignupDto,
    ): ResponsePostSignupDto
}