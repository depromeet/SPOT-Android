package com.dpm.presentation.viewfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import com.dpm.domain.entity.response.seatreview.ResponseStadiumSection
import com.dpm.domain.entity.response.viewfinder.ResponseStadium
import com.dpm.domain.repository.ViewfinderRepository
import com.dpm.domain.repository.WebSvgRepository
import com.dpm.presentation.util.getHTMLBody
import com.dpm.presentation.viewfinder.adapter.MockSection
import com.dpm.presentation.viewfinder.adapter.Recommend
import com.dpm.presentation.viewfinder.adapter.StadiumSectionRecommendAdapter
import com.dpm.presentation.viewfinder.adapter.mockSections
import com.dpm.presentation.viewfinder.adapter.recommend
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

    private val _blockFilters = MutableStateFlow<List<Recommend>>(emptyList())
    val blockFilters = _blockFilters.asStateFlow()

    private val _sections = MutableStateFlow<List<MockSection>>(emptyList())
    val sections = _sections.asStateFlow()

    fun downloadFileFromServer(url: String) {
        viewModelScope.launch {
//            webSvgRepository.downloadFileWithDynamicUrlAsync(url).let { svgString ->
            webSvgRepository.downloadFileWithDynamicUrlAsync("https://svgshare.com/i/19Q3.svg")
                .let { svgString ->
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

    fun getBlockFilters() {
        _blockFilters.value = recommend
    }

    fun getSection() {
        _sections.value = mockSections
    }

    fun updateBlockFilter(recommend: Recommend) {
        _blockFilters.value = _blockFilters.value.map {
            if (it.id == recommend.id) {
                it.copy(isActive = !recommend.isActive)
            } else {
                it.copy(isActive = false)
            }
        }
    }

    fun updateSections(section: MockSection) {
        _sections.value = _sections.value.map {
            if (it.id == section.id) {
                it.copy(isActive = !section.isActive)
            } else {
                it.copy(isActive = false)
            }
        }
    }

    fun refreshFilter() {
        _blockFilters.value = _blockFilters.value.map {
            it.copy(isActive = false)
        }
    }

    fun refreshSections() {
        _sections.value = _sections.value.map {
            it.copy(isActive = false)
        }
    }
}