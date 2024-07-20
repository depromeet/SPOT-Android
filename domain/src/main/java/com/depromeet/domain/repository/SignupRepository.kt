package com.depromeet.domain.repository

import com.depromeet.domain.entity.request.signup.PostSignupModel
import com.depromeet.domain.entity.response.signup.SignupTokenModel

interface SignupRepository {
    suspend fun postSignup(
        postSignupModel: PostSignupModel
    ): Result<SignupTokenModel>
}