package com.depromeet.data.datasource.remote

import com.depromeet.data.datasource.HomeDataSource
import com.depromeet.data.model.request.home.RequestFileExtensionDto
import com.depromeet.data.model.request.home.RequestMySeatRecordDto
import com.depromeet.data.model.request.home.RequestProfileEditDto
import com.depromeet.data.model.response.home.ResponseBaseballTeamDto
import com.depromeet.data.model.response.home.ResponseMySeatRecordDto
import com.depromeet.data.model.response.home.ResponsePresignedUrlDto
import com.depromeet.data.model.response.home.ResponseProfileDto
import com.depromeet.data.model.response.home.ResponseProfileEditDto
import com.depromeet.data.model.response.home.ResponseRecentReviewDto
import com.depromeet.data.model.response.home.ResponseReviewDateDto
import com.depromeet.data.remote.HomeApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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

    override suspend fun postProfileImagePresigned(
        fileExtension: String,
        memberId: Int,
    ): ResponsePresignedUrlDto {
        return homeApiService.postProfileImagePresigned(
            RequestFileExtensionDto(fileExtension),
            memberId
        )
    }

    override suspend fun putProfileImage(presignedUrl: String, image: ByteArray) {
        val mediaType = "image/*".toMediaTypeOrNull()
        return homeApiService.putProfileImage(presignedUrl, image.toRequestBody(mediaType))
    }

    override suspend fun putProfileEdit(
        requestProfileEditDto: RequestProfileEditDto,
        memberId: Int,
    ): ResponseProfileEditDto {
        return homeApiService.putProfileEdit(requestProfileEditDto, memberId)
    }

    override suspend fun getDuplicateNickname(nickname: String) {
        return homeApiService.getDuplicateNickname(nickname)
    }

    override suspend fun getReviewDate(): ResponseReviewDateDto {
        return homeApiService.getReviewDate()
    }

    override suspend fun getProfile(): ResponseProfileDto {
        return homeApiService.getProfileInfo()
    }

    override suspend fun getRecentReview(): ResponseRecentReviewDto {
        return homeApiService.getRecentReview()
    }
}