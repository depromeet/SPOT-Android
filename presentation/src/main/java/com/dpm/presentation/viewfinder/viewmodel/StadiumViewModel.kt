package com.dpm.presentation.viewfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import com.dpm.domain.entity.response.viewfinder.ResponseStadium
import com.dpm.domain.entity.response.viewfinder.Section
import com.dpm.domain.repository.ViewfinderRepository
import com.dpm.domain.repository.WebSvgRepository
import com.dpm.presentation.util.SingleLiveEvent
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

    private val _htmlBody = SingleLiveEvent<UiState<String>>()
    val htmlBody = _htmlBody

    private val _stadium = SingleLiveEvent<UiState<ResponseStadium>>()
    val stadium = _stadium

    private val _blockFilters =
        SingleLiveEvent<List<ResponseStadium.ResponseBlockTags>>()
    val blockFilters = _blockFilters

    private val _sections = SingleLiveEvent<List<Section>>()
    val sections = _sections

    fun downloadFileFromServer(url: String) {
        viewModelScope.launch {
            _htmlBody.value = UiState.Loading
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
            _stadium.value = UiState.Loading
            viewfinderRepository.getStadium(id).onSuccess { stadium ->
                _blockFilters.value = stadium.blockTags
                _sections.value = stadium.sections
                _stadium.value = UiState.Success(stadium)
            }.onFailure { e ->
                _stadium.value = UiState.Failure(e.message ?: "")
            }
        }
    }

    fun setBlockFilters(blockFilters: List<ResponseStadium.ResponseBlockTags>) {
        _blockFilters.value = blockFilters
    }

    fun setSections(sections: List<Section>) {
        _sections.value = sections
    }

    fun updateBlockFilter(recommend: ResponseStadium.ResponseBlockTags) {
        _blockFilters.value = _blockFilters.value?.map {
            if (it.id == recommend.id) {
                it.copy(isActive = !recommend.isActive)
            } else {
                it.copy(isActive = false)
            }
        } ?: return
    }

    fun updateSections(section: Section) {
        _sections.value = _sections.value?.map {
            if (it.id == section.id) {
                it.copy(isActive = !section.isActive)
            } else {
                it.copy(isActive = false)
            }
        } ?: return
    }

    fun refreshFilter() {
        _blockFilters.value = _blockFilters.value?.map {
            it.copy(isActive = false)
        } ?: return
    }

    fun refreshSections() {
        _sections.value = _sections.value?.map {
            it.copy(isActive = false)
        } ?: return
    }
}