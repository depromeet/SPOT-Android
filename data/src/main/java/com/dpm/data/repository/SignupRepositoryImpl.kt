package com.dpm.data.repository

import com.dpm.data.datasource.SignupRemoteDataSource
import com.dpm.data.model.request.signup.toRequestPostSignupDto
import com.dpm.data.model.response.signup.toSignupTokenModel
import com.dpm.domain.entity.request.signup.PostSignupModel
import com.dpm.domain.entity.response.signup.SignupTokenModel
import com.dpm.domain.repository.SignupRepository
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

    override suspend fun deleteWithdraw(): Result<Unit> {
        return runCatching {
            signupRemoteDataSource.deleteWithdraw()
        }
    }
}