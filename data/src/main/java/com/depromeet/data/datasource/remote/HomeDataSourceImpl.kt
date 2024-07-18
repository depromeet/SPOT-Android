package com.depromeet.data.datasource.remote

import com.depromeet.data.datasource.HomeDataSource
import com.depromeet.data.model.request.RequestMySeatRecordDto
import com.depromeet.data.model.response.home.ResponseBaseballTeamDto
import com.depromeet.data.model.response.home.ResponseMySeatRecordDto
import com.depromeet.data.remote.HomeApiService
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(
    private val homeApiService: HomeApiService,
) : HomeDataSource {
    override suspend fun getMySeatRecordData(requestMySeatRecordDto: RequestMySeatRecordDto): ResponseMySeatRecordDto {
        return homeApiService.getMySeatRecord(
            requestMySeatRecordDto.offset,
            requestMySeatRecordDto.limit,
            requestMySeatRecordDto.year,
            requestMySeatRecordDto.month
        )
    }

    override suspend fun getBaseballTeamData(): List<ResponseBaseballTeamDto> {
        return homeApiService.getBaseballTeam()
    }

    override suspend fun postProfileImagePresigned(fileExtension: String, memberId: Int): String {
        return homeApiService.postProfileImagePresigned(fileExtension, memberId)
    }

}