package com.depromeet.data.repository

import com.depromeet.data.datasource.HomeDataSource
import com.depromeet.data.mapper.toBaseballTeamResponse
import com.depromeet.data.mapper.toMySeatRecordRequestDto
import com.depromeet.data.mapper.toPresignedUrlResponse
import com.depromeet.data.model.request.home.RequestProfileEditDto.Companion.toProfileEditRequestDto
import com.depromeet.data.model.response.home.ResponseMySeatRecordDto.Companion.toMySeatRecordResponse
import com.depromeet.data.model.response.home.ResponseProfileEditDto.Companion.toProfileEditResponse
import com.depromeet.data.model.response.home.ResponseReviewDateDto.Companion.toReviewDateResponse
import com.depromeet.domain.entity.request.home.MySeatRecordRequest
import com.depromeet.domain.entity.request.home.ProfileEditRequest
import com.depromeet.domain.entity.response.home.BaseballTeamResponse
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.domain.entity.response.home.PresignedUrlResponse
import com.depromeet.domain.entity.response.home.ProfileEditResponse
import com.depromeet.domain.entity.response.home.ReviewDateResponse
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

    override suspend fun getBaseballTeam(): Result<List<BaseballTeamResponse>> {
        return runCatching {
            homeDataSource.getBaseballTeamData().map { it.toBaseballTeamResponse() }
        }
    }

    override suspend fun postProfileImagePresigned(
        fileExtension: String,
        memberId: Int,
    ): Result<PresignedUrlResponse> {
        return runCatching {
            homeDataSource.postProfileImagePresigned(fileExtension, memberId)
                .toPresignedUrlResponse()
        }
    }

    override suspend fun putProfileImage(presignedUrl: String, image: ByteArray): Result<Unit> {
        return runCatching {
            homeDataSource.putProfileImage(presignedUrl, image)
        }
    }

    override suspend fun putProfileEdit(
        profileEditRequest: ProfileEditRequest,
        memberId: Int,
    ): Result<ProfileEditResponse> {
        return runCatching {
            homeDataSource.putProfileEdit(profileEditRequest.toProfileEditRequestDto(), memberId)
                .toProfileEditResponse()
        }
    }

    override suspend fun getDuplicateNickname(nickname: String): Result<Unit> {
        return runCatching {
            homeDataSource.getDuplicateNickname(nickname)
        }
    }

    override suspend fun getReviewDate(): Result<ReviewDateResponse> {
        return runCatching {
            homeDataSource.getReviewDate().toReviewDateResponse()
        }
    }
}