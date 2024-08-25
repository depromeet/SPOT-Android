package com.dpm.data.datasource

interface AuthDataSource {
    suspend fun getToken(): String
}