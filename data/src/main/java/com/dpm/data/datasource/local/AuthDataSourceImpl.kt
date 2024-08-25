package com.dpm.data.datasource.local

import com.dpm.data.datasource.AuthDataSource
import com.dpm.domain.preference.SharedPreference
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val sharedPreference: SharedPreference
): AuthDataSource {
    override suspend fun getToken(): String {
        return sharedPreference.token
    }
}