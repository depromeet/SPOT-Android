package com.depromeet.data.datasource

import com.depromeet.data.model.request.home.RequestMySeatRecordDto
import com.depromeet.data.model.request.home.RequestProfileEditDto
import com.depromeet.data.model.response.home.ResponseBaseballTeamDto
import com.depromeet.data.model.response.home.ResponseMySeatRecordDto
import com.depromeet.data.model.response.home.ResponsePresignedUrlDto
import com.depromeet.data.model.response.home.ResponseProfileEditDto
import com.depromeet.data.model.response.home.ResponseReviewDateDto

interface HomeDataSource {
    suspend fun getMySeatRecordData(
        requestMySeatRecordDto: RequestMySeatRecordDto,
    ): ResponseMySeatRecordDto

    suspend fun getBaseballTeamData(): List<ResponseBaseballTeamDto>

    suspend fun postProfileImagePresigned(
        fileExtension: String,
        memberId: Int,
    ): ResponsePresignedUrlDto

    suspend fun putProfileImage(
        presignedUrl: String,
        image: ByteArray,
    )

    suspend fun putProfileEdit(
        requestProfileEditDto: RequestProfileEditDto,
        memberId: Int,
    ): ResponseProfileEditDto

    suspend fun getDuplicateNickname(
        nickname: String,
    )

    suspend fun getReviewDate(): ResponseReviewDateDto
}