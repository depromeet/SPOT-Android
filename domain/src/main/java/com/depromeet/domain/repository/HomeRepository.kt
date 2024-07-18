package com.depromeet.domain.repository

import com.depromeet.domain.entity.request.home.MySeatRecordRequest
import com.depromeet.domain.entity.response.home.MySeatRecordResponse

interface HomeRepository {
    suspend fun getMySeatRecord(
        mySeatRecordRequest: MySeatRecordRequest,
    ): Result<MySeatRecordResponse>
}