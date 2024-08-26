package com.dpm.domain.repository

import com.dpm.domain.entity.request.home.RequestMySeatRecord
import com.dpm.domain.entity.request.home.RequestProfileEdit
import com.dpm.domain.entity.response.home.ResponseBaseballTeam
import com.dpm.domain.entity.response.home.ResponseDeleteReview
import com.dpm.domain.entity.response.home.ResponseHomeFeed
import com.dpm.domain.entity.response.home.ResponseLevelByPost
import com.dpm.domain.entity.response.home.ResponseLevelUpInfo
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.domain.entity.response.home.ResponsePresignedUrl
import com.dpm.domain.entity.response.home.ResponseProfileEdit
import com.dpm.domain.entity.response.home.ResponseRecentReview
import com.dpm.domain.entity.response.home.ResponseReviewDate
import com.dpm.domain.entity.response.home.ResponseUserInfo

interface HomeRepository {
    suspend fun getMySeatRecord(
        requestMySeatRecord: RequestMySeatRecord,
    ): Result<ResponseMySeatRecord>

    suspend fun getBaseballTeam(): Result<List<ResponseBaseballTeam>>

    suspend fun postProfileImagePresigned(
        fileExtension: String,
    ): Result<ResponsePresignedUrl>

    suspend fun putProfileImage(
        presignedUrl: String,
        image: ByteArray,
    ): Result<Unit>

    suspend fun putProfileEdit(
        requestProfileEdit: RequestProfileEdit,
    ): Result<ResponseProfileEdit>

    suspend fun getDuplicateNickname(
        nickname: String,
    ): Result<Unit>

    suspend fun getReviewDate(): Result<ResponseReviewDate>

    suspend fun getRecentReview(): Result<ResponseRecentReview>

    suspend fun deleteReview(
        reviewId: Int,
    ): Result<ResponseDeleteReview>

    suspend fun getLevelByPost(): Result<List<ResponseLevelByPost>>

    suspend fun getHomeFeed(): Result<ResponseHomeFeed>

    suspend fun getLevelUpInfo(nextLevel: Int): Result<ResponseLevelUpInfo>

    suspend fun getMyUserInfo(): Result<ResponseUserInfo>
}