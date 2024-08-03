package com.depromeet.data.remote

import com.depromeet.data.model.request.signup.RequestPostSignupDto
import com.depromeet.data.model.response.signup.ResponsePostSignupDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SignupService {

    @POST("/api/v1/members")
    suspend fun postSignupMember(
        @Body requestPostSignupDto: RequestPostSignupDto,
    ): ResponsePostSignupDto

    @GET("/api/v1/members/{accessToken}")
    suspend fun getSignupMember(
        @Path("accessToken") accessToken: String,
    ): ResponsePostSignupDto

    @DELETE("/api/v1/members")
    suspend fun deleteSignupMember()
}