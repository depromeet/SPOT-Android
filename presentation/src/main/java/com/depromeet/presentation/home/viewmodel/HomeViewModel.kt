package com.depromeet.presentation.home.viewmodel

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.ProfileResponse
import com.depromeet.domain.entity.response.home.RecentReviewResponse
import com.depromeet.domain.repository.HomeRepository
import com.depromeet.presentation.home.mockdata.Profile
import com.depromeet.presentation.home.mockdata.RecentSight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class HomeUiState(
    val profile: Profile = Profile(),
    val recentSight: RecentSight = RecentSight(),
) : Parcelable

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _profile = MutableStateFlow<UiState<ProfileResponse>>(UiState.Loading)
    val profile = _profile.asStateFlow()

    private val _recentReview = MutableStateFlow<UiState<RecentReviewResponse>>(UiState.Loading)
    val recentReview = _recentReview.asStateFlow()

    val nickname = MutableStateFlow<String>("")
    val reviewCount = MutableStateFlow(0)

    fun getInformation() {
        viewModelScope.launch {
            val profileDeferred = async {
                homeRepository.getProfile()
            }
            val reviewDeferred = async {
                homeRepository.getRecentReview()
            }

            val profileResult = profileDeferred.await()
            profileResult.onSuccess {
                _profile.value = UiState.Success(it)
            }.onFailure {
                _profile.value = UiState.Failure(it.message ?: "실패")
            }

            val reviewResult = reviewDeferred.await()
            reviewResult.onSuccess {
                _recentReview.value = UiState.Success(it)
            }.onFailure {
                _recentReview.value = UiState.Failure(it.message ?: "실패")
            }
        }
    }
}