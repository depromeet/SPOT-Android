package com.depromeet.presentation.viewfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.viewfinder.StadiumResponse
import com.depromeet.domain.repository.ViewfinderRepository
import com.depromeet.domain.repository.WebSvgRepository
import com.depromeet.presentation.util.getHTMLBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StadiumViewModel @Inject constructor(
    private val webSvgRepository: WebSvgRepository,
    private val viewfinderRepository: ViewfinderRepository
) : ViewModel() {
    var stadiumId: Int = 0

    private val _htmlBody = MutableStateFlow<UiState<String>>(UiState.Loading)
    val htmlBody = _htmlBody.asStateFlow()

    private val _stadium = MutableStateFlow<UiState<StadiumResponse>>(UiState.Loading)
    val stadium = _stadium.asStateFlow()

    fun downloadFileFromServer(url: String) {
        viewModelScope.launch {
            webSvgRepository.downloadFileWithDynamicUrlAsync(url).let { svgString ->
                svgString.onSuccess {
                    _htmlBody.value = UiState.Success(it)
                }.onFailure {
                    _htmlBody.value = UiState.Failure(it.message.toString())
                }
            }
        }
    }

    fun getStadium(id: Int) {
        viewModelScope.launch {
            viewfinderRepository.getStadium(id).onSuccess { stadium ->
                _stadium.value = UiState.Success(stadium)
            }.onFailure { e ->
                _stadium.value = UiState.Failure(e.message ?: "")
            }
        }
    }
}
