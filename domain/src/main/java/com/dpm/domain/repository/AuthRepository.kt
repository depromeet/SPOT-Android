package com.dpm.domain.repository

interface AuthRepository {
    suspend fun getToken(): Result<String>
}