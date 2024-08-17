package com.dpm.domain.repository

interface WebSvgRepository {
    suspend fun downloadFileWithDynamicUrlAsync(url: String): Result<String>
}