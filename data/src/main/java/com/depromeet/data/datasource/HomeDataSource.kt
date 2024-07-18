package com.depromeet.data.datasource

import com.depromeet.data.model.request.RequestMySeatRecordDto
import com.depromeet.data.model.response.home.ResponseBaseballTeamDto
import com.depromeet.data.model.response.home.ResponseMySeatRecordDto

interface HomeDataSource {
    suspend fun getMySeatRecordData(
        requestMySeatRecordDto: RequestMySeatRecordDto,
    ): ResponseMySeatRecordDto

    suspend fun getBaseballTeamData(): List<ResponseBaseballTeamDto>
}