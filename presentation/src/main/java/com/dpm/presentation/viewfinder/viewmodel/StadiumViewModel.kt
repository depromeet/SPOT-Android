package com.dpm.presentation.viewfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import com.dpm.domain.entity.response.viewfinder.ResponseStadium
import com.dpm.domain.repository.ViewfinderRepository
import com.dpm.domain.repository.WebSvgRepository
import com.dpm.presentation.util.getHTMLBody
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

    private val _stadium = MutableStateFlow<UiState<ResponseStadium>>(UiState.Loading)
    val stadium = _stadium.asStateFlow()

    fun downloadFileFromServer(url: String) {
        viewModelScope.launch {
            webSvgRepository.downloadFileWithDynamicUrlAsync(url).let { svgString ->
                svgString.onSuccess {
                    _htmlBody.value = UiState.Success(getHTMLBody(it))
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
