package com.dpm.data.datasource

import com.dpm.data.model.request.home.RequestMySeatRecordDto
import com.dpm.data.model.request.home.RequestProfileEditDto
import com.dpm.data.model.response.home.ResponseBaseballTeamDto
import com.dpm.data.model.response.home.ResponseDeleteReviewDto
import com.dpm.data.model.response.home.ResponseHomeFeedDto
import com.dpm.data.model.response.home.ResponseLevelByPostDto
import com.dpm.data.model.response.home.ResponseLevelUpInfoDto
import com.dpm.data.model.response.home.ResponseMySeatRecordDto
import com.dpm.data.model.response.home.ResponsePresignedUrlDto
import com.dpm.data.model.response.home.ResponseProfileDto
import com.dpm.data.model.response.home.ResponseProfileEditDto
import com.dpm.data.model.response.home.ResponseRecentReviewDto
import com.dpm.data.model.response.home.ResponseReviewDateDto

interface HomeDataSource {
    suspend fun getMySeatRecordData(
        requestMySeatRecordDto: RequestMySeatRecordDto,
    ): ResponseMySeatRecordDto

    suspend fun getBaseballTeamData(): List<ResponseBaseballTeamDto>

    suspend fun postProfileImagePresigned(
        fileExtension: String,
    ): ResponsePresignedUrlDto

    suspend fun putProfileImage(
        presignedUrl: String,
        image: ByteArray,
    )

    suspend fun putProfileEdit(
        requestProfileEditDto: RequestProfileEditDto,
    ): ResponseProfileEditDto

    suspend fun getDuplicateNickname(
        nickname: String,
    )

    suspend fun getReviewDate(): ResponseReviewDateDto

    suspend fun getProfile(): ResponseProfileDto

    suspend fun getRecentReview(): ResponseRecentReviewDto

    suspend fun deleteReview(
        reviewId: Int,
    ): ResponseDeleteReviewDto

    suspend fun getLevelByPost(): List<ResponseLevelByPostDto>

    suspend fun getHomeFeed() : ResponseHomeFeedDto

    suspend fun getLevelUpInfo(nextLevel : Int) : ResponseLevelUpInfoDto
}