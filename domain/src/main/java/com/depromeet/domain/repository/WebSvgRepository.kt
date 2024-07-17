package com.depromeet.domain.repository

interface WebSvgRepository {
    suspend fun downloadFileWithDynamicUrlAsync(url: String): Result<String>
}