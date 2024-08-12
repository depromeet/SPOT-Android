package com.depromeet.data.repository

import com.depromeet.data.datasource.HomeDataSource
import com.depromeet.data.mapper.toBaseballTeamResponse
import com.depromeet.data.mapper.toPresignedUrlResponse
import com.depromeet.data.model.request.home.RequestMySeatRecordDto.Companion.toMySeatRecordRequestDto
import com.depromeet.data.model.request.home.RequestProfileEditDto.Companion.toProfileEditRequestDto
import com.depromeet.data.model.response.home.ResponseDeleteReviewDto.Companion.toDeleteReviewResponse
import com.depromeet.data.model.response.home.ResponseMySeatRecordDto.Companion.toMySeatRecordResponse
import com.depromeet.data.model.response.home.ResponseProfileDto.Companion.toProfileResponse
import com.depromeet.data.model.response.home.ResponseProfileEditDto.Companion.toProfileEditResponse
import com.depromeet.data.model.response.home.ResponseRecentReviewDto.Companion.toRecentReviewResponse
import com.depromeet.data.model.response.home.ResponseReviewDateDto.Companion.toReviewDateResponse
import com.depromeet.domain.entity.request.home.RequestMySeatRecord
import com.depromeet.domain.entity.request.home.RequestProfileEdit
import com.depromeet.domain.entity.response.home.ResponseBaseballTeam
import com.depromeet.domain.entity.response.home.ResponseDeleteReview
import com.depromeet.domain.entity.response.home.ResponseHomeFeed
import com.depromeet.domain.entity.response.home.ResponseLevelByPost
import com.depromeet.domain.entity.response.home.ResponseLevelUpInfo
import com.depromeet.domain.entity.response.home.ResponseMySeatRecord
import com.depromeet.domain.entity.response.home.ResponsePresignedUrl
import com.depromeet.domain.entity.response.home.ResponseProfileEdit
import com.depromeet.domain.entity.response.home.ResponseProfile
import com.depromeet.domain.entity.response.home.ResponseRecentReview
import com.depromeet.domain.entity.response.home.ResponseReviewDate
import com.depromeet.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource,
) : HomeRepository {
    override suspend fun getMySeatRecord(requestMySeatRecord: RequestMySeatRecord): Result<ResponseMySeatRecord> {
        return runCatching {
            homeDataSource.getMySeatRecordData(requestMySeatRecord.toMySeatRecordRequestDto())
                .toMySeatRecordResponse()
        }
    }

    override suspend fun getBaseballTeam(): Result<List<ResponseBaseballTeam>> {
        return runCatching {
            homeDataSource.getBaseballTeamData().map { it.toBaseballTeamResponse() }
        }
    }

    override suspend fun postProfileImagePresigned(
        fileExtension: String,
    ): Result<ResponsePresignedUrl> {
        return runCatching {
            homeDataSource.postProfileImagePresigned(fileExtension)
                .toPresignedUrlResponse()
        }
    }

    override suspend fun putProfileImage(presignedUrl: String, image: ByteArray): Result<Unit> {
        return runCatching {
            homeDataSource.putProfileImage(presignedUrl, image)
        }
    }

    override suspend fun putProfileEdit(
        requestProfileEdit: RequestProfileEdit,
    ): Result<ResponseProfileEdit> {
        return runCatching {
            homeDataSource.putProfileEdit(requestProfileEdit.toProfileEditRequestDto())
                .toProfileEditResponse()
        }
    }

    override suspend fun getDuplicateNickname(nickname: String): Result<Unit> {
        return runCatching {
            homeDataSource.getDuplicateNickname(nickname)
        }
    }

    override suspend fun getReviewDate(): Result<ResponseReviewDate> {
        return runCatching {
            homeDataSource.getReviewDate().toReviewDateResponse()
        }
    }

    override suspend fun getProfile(): Result<ResponseProfile> {
        return runCatching {
            homeDataSource.getProfile().toProfileResponse()
        }
    }

    override suspend fun getRecentReview(): Result<ResponseRecentReview> {
        return runCatching {
            homeDataSource.getRecentReview().toRecentReviewResponse()
        }
    }

    override suspend fun deleteReview(reviewId: Int): Result<ResponseDeleteReview> {
        return runCatching {
            homeDataSource.deleteReview(reviewId).toDeleteReviewResponse()
        }
    }

    override suspend fun getLevelByPost(): Result<List<ResponseLevelByPost>> {
        return runCatching {
            homeDataSource.getLevelByPost().map { it.toLevelByPostResponse() }
        }
    }

    override suspend fun getHomeFeed(): Result<ResponseHomeFeed> {
        return runCatching {
            homeDataSource.getHomeFeed().toHomeFeedResponse()
        }
    }

    override suspend fun getLevelUpInfo(nextLevel: Int): Result<ResponseLevelUpInfo> {
        return runCatching {
            homeDataSource.getLevelUpInfo(nextLevel).toLevelUpInfoResponse()
        }
    }
}