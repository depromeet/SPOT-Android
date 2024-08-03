package com.depromeet.domain.repository

import com.depromeet.domain.entity.request.home.MySeatRecordRequest
import com.depromeet.domain.entity.request.home.ProfileEditRequest
import com.depromeet.domain.entity.response.home.BaseballTeamResponse
import com.depromeet.domain.entity.response.home.DeleteReviewResponse
import com.depromeet.domain.entity.response.home.LevelByPostResponse
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.domain.entity.response.home.PresignedUrlResponse
import com.depromeet.domain.entity.response.home.ProfileEditResponse
import com.depromeet.domain.entity.response.home.ProfileResponse
import com.depromeet.domain.entity.response.home.RecentReviewResponse
import com.depromeet.domain.entity.response.home.ReviewDateResponse

interface HomeRepository {
    suspend fun getMySeatRecord(
        mySeatRecordRequest: MySeatRecordRequest,
    ): Result<MySeatRecordResponse>

    suspend fun getBaseballTeam(): Result<List<BaseballTeamResponse>>

    suspend fun postProfileImagePresigned(
        fileExtension: String,
    ): Result<PresignedUrlResponse>

    suspend fun putProfileImage(
        presignedUrl: String,
        image: ByteArray,
    ): Result<Unit>

    suspend fun putProfileEdit(
        profileEditRequest: ProfileEditRequest,
    ): Result<ProfileEditResponse>

    suspend fun getDuplicateNickname(
        nickname: String,
    ): Result<Unit>

    suspend fun getReviewDate(): Result<ReviewDateResponse>

    suspend fun getProfile(): Result<ProfileResponse>

    suspend fun getRecentReview(): Result<RecentReviewResponse>

    suspend fun deleteReview(
        reviewId: Int,
    ): Result<DeleteReviewResponse>

    suspend fun getLevelByPost() : Result<List<LevelByPostResponse>>
}