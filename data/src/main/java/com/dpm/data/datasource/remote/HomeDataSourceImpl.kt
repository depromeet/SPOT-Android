package com.dpm.data.datasource.remote

import com.dpm.data.datasource.HomeDataSource
import com.dpm.data.model.request.home.RequestFileExtensionDto
import com.dpm.data.model.request.home.RequestMySeatRecordDto
import com.dpm.data.model.request.home.RequestProfileEditDto
import com.dpm.data.model.response.home.ResponseBaseballTeamDto
import com.dpm.data.model.response.home.ResponseDeleteReviewDto
import com.dpm.data.model.response.home.ResponseHomeFeedDto
import com.dpm.data.model.response.home.ResponseLevelByPostDto
import com.dpm.data.model.response.home.ResponseLevelUpInfoDto
import com.dpm.data.model.response.home.ResponseMySeatRecordDto
import com.dpm.data.model.response.home.ResponsePresignedUrlDto
import com.dpm.data.model.response.home.ResponseProfileEditDto
import com.dpm.data.model.response.home.ResponseRecentReviewDto
import com.dpm.data.model.response.home.ResponseReviewDateDto
import com.dpm.data.model.response.home.ResponseUserInfoDto
import com.dpm.data.remote.HomeApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(
    private val homeApiService: HomeApiService,
) : HomeDataSource {
    override suspend fun getMySeatRecordData(requestMySeatRecordDto: RequestMySeatRecordDto): ResponseMySeatRecordDto {
        return homeApiService.getMySeatRecord(
            cursor = requestMySeatRecordDto.cursor,
            sortBy = requestMySeatRecordDto.sortBy,
            size = requestMySeatRecordDto.size,
            year = requestMySeatRecordDto.year,
            month = requestMySeatRecordDto.month,
            reviewType = requestMySeatRecordDto.reviewType
        )
    }


    override suspend fun getBaseballTeamData(): List<ResponseBaseballTeamDto> {
        return homeApiService.getBaseballTeam()
    }

    override suspend fun postProfileImagePresigned(
        fileExtension: String,
    ): ResponsePresignedUrlDto {
        return homeApiService.postProfileImagePresigned(
            RequestFileExtensionDto(fileExtension),
        )
    }

    override suspend fun putProfileImage(presignedUrl: String, image: ByteArray) {
        val mediaType = "image/*".toMediaTypeOrNull()
        return homeApiService.putProfileImage(presignedUrl, image.toRequestBody(mediaType))
    }

    override suspend fun putProfileEdit(
        requestProfileEditDto: RequestProfileEditDto,
    ): ResponseProfileEditDto {
        return homeApiService.putProfileEdit(requestProfileEditDto)
    }

    override suspend fun getDuplicateNickname(nickname: String) {
        return homeApiService.getDuplicateNickname(nickname)
    }

    override suspend fun getReviewDate(reviewType : String?): ResponseReviewDateDto {
        return homeApiService.getReviewDate(reviewType)
    }


    override suspend fun getRecentReview(): ResponseRecentReviewDto {
        return homeApiService.getRecentReview()
    }

    override suspend fun deleteReview(
        reviewId: Int,
    ): ResponseDeleteReviewDto {
        return homeApiService.deleteReview(reviewId)
    }

    override suspend fun getLevelByPost(): List<ResponseLevelByPostDto> {
        return homeApiService.getLevelByPost()
    }

    override suspend fun getHomeFeed(): ResponseHomeFeedDto {
        return homeApiService.getHomeFeed()
    }

    override suspend fun getLevelUpInfo(nextLevel: Int): ResponseLevelUpInfoDto {
        return homeApiService.getLevelUpInfo(nextLevel)
    }

    override suspend fun getUserInfo(): ResponseUserInfoDto {
        return homeApiService.getUserInfo()
    }
}