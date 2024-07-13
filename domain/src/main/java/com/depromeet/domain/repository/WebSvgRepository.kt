package com.depromeet.domain.repository

import kotlinx.coroutines.flow.Flow

interface WebSvgRepository {
    fun downloadFileWithDynamicUrlAsync(url: String): Flow<String>
}