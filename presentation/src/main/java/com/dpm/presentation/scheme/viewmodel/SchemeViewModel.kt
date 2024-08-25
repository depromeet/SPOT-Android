package com.dpm.presentation.scheme.viewmodel

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.domain.usecase.GetTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class SchemeViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<SchemeState>(SchemeState.Nothing)
    val state = _state.asStateFlow()

    fun navigateToDetailReview(
        stadiumId: Int,
        blockCode: String
    ) {
        viewModelScope.launch {
            getTokenUseCase().onSuccess {
                if (it.isEmpty()) {
                    // 비로그인 상태
                    _state.value = SchemeState.NonLogin(
                        SchemeState.NavReview(stadiumId, blockCode)
                    )
                } else {
                    // 로그인 상태
                    _state.value = SchemeState.NavReview(stadiumId, blockCode)
                }
            }.onFailure {
                _state.value = SchemeState.Failed
            }
        }
    }
}

sealed interface SchemeState {
    @Parcelize
    data class NavReview(
        val stadiumId: Int,
        val blockCode: String
    ) : SchemeState, Parcelable

    @Parcelize
    data class NavReviewDetail(
        val stadiumId: Int,
        val blockCode: String,
        val reviewId: Int
    ) : SchemeState, Parcelable

    data class NonLogin<T>(val data: T) : SchemeState
    object Failed: SchemeState
    object Nothing : SchemeState
}
