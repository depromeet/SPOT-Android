package com.depromeet.data.remote

import com.depromeet.data.model.request.signup.RequestPostSignupDto
import com.depromeet.data.model.response.signup.ResponsePostSignupDto
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupService {

    @POST("/api/v1/members")
    suspend fun postSignupMember(
        @Body requestPostSignupDto: RequestPostSignupDto,
    ): ResponsePostSignupDto
}