package com.depromeet.data.repository

import com.depromeet.data.datasource.SignupRemoteDataSource
import com.depromeet.data.model.request.signup.toRequestPostSignupDto
import com.depromeet.data.model.response.signup.toSignupTokenModel
import com.depromeet.domain.entity.request.signup.PostSignupModel
import com.depromeet.domain.entity.response.signup.SignupTokenModel
import com.depromeet.domain.repository.SignupRepository
import javax.inject.Inject

class SignupRepositoryImpl @Inject constructor(
    private val signupRemoteDataSource: SignupRemoteDataSource
): SignupRepository {
    override suspend fun postSignup(postSignupModel: PostSignupModel): Result<SignupTokenModel> {
        return runCatching {
            signupRemoteDataSource.postSignup(postSignupModel.toRequestPostSignupDto()).toSignupTokenModel()
        }
    }

    override suspend fun getSignup(accessToken: String): Result<SignupTokenModel> {
        return runCatching {
            signupRemoteDataSource.getSignup(accessToken).toSignupTokenModel()
        }
    }
}