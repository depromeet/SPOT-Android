package com.depromeet.data.repository

import com.depromeet.data.datasource.HomeDataSource
import com.depromeet.data.mapper.toMySeatRecordRequestDto
import com.depromeet.data.mapper.toMySeatRecordResponse
import com.depromeet.domain.entity.request.home.MySeatRecordRequest
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource,
) : HomeRepository {
    override suspend fun getMySeatRecord(mySeatRecordRequest: MySeatRecordRequest): Result<MySeatRecordResponse> {
        return runCatching {
            homeDataSource.getMySeatRecordData(mySeatRecordRequest.toMySeatRecordRequestDto())
                .toMySeatRecordResponse()
        }
    }
}