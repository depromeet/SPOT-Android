package com.depromeet.presentation.viewfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.repository.WebSvgRepository
import com.depromeet.presentation.util.getHTMLBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StadiumViewModel @Inject constructor(
    private val webSvgRepository: WebSvgRepository
) : ViewModel() {
    private val _htmlBody = MutableStateFlow<UiState<String>>(UiState.Loading)
    val htmlBody = _htmlBody.asStateFlow()

    fun downloadFileFromServer(url: String) {
        viewModelScope.launch {
            webSvgRepository.downloadFileWithDynamicUrlAsync(url).let { svgString ->
                _htmlBody.value = if (svgString.isEmpty()) UiState.Failure("") else UiState.Success(
                    getHTMLBody(svgString)
                )
            }
        }
    }
}
