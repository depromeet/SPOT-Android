package com.dpm.data.repository

import com.dpm.data.datasource.AuthDataSource
import com.dpm.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {
    override suspend fun getToken(): Result<String> {
        return runCatching {
            authDataSource.getToken()
        }
    }
}