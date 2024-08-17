package com.dpm.domain.repository

import com.dpm.domain.entity.request.signup.PostSignupModel
import com.dpm.domain.entity.response.signup.SignupTokenModel

interface SignupRepository {
    suspend fun postSignup(
        postSignupModel: PostSignupModel
    ): Result<SignupTokenModel>

    suspend fun getSignup(
        accessToken: String
    ): Result<SignupTokenModel>

    suspend fun deleteWithdraw(): Result<Unit>
}