package com.dpm.presentation.scrap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import com.dpm.domain.entity.request.home.RequestScrap
import com.dpm.domain.entity.response.home.ResponseScrap
import com.dpm.domain.repository.HomeRepository
import com.dpm.domain.repository.ViewfinderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class FilterNameData(
    val filterType: ScrapViewModel.ScrapFilterType,
    val name: String,
)

data class MonthFilterData(
    val month: Int,
    val isSelected: Boolean,
) {
    fun formattedMonth(): String {
        return "${month}월"
    }
}

data class GoodReviewData(
    val name: String,
)

data class BadReviewData(
    val name: String,
)

@HiltViewModel
class ScrapViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val viewfinderRepository: ViewfinderRepository,
) : ViewModel() {

    private var monthsSelected: List<MonthFilterData> = emptyList()
    private var goodSelected: List<GoodReviewData> = emptyList()
    private var badSelected: List<BadReviewData> = emptyList()


    private val _scrap = MutableStateFlow<UiState<ResponseScrap>>(UiState.Loading)
    val scrap = _scrap.asStateFlow()

    private val _filter = MutableStateFlow<List<FilterNameData>>(emptyList())
    val filter = _filter.asStateFlow()

    private val _months = MutableStateFlow<List<MonthFilterData>>(emptyList())
    val months = _months.asStateFlow()

    private val _selectedGoodReview = MutableStateFlow<List<GoodReviewData>>(emptyList())
    val selectedGoodReview = _selectedGoodReview.asStateFlow()

    private val _selectedBadReview = MutableStateFlow<List<BadReviewData>>(emptyList())
    val selectedBadReview = _selectedBadReview.asStateFlow()


    fun getScrapRecord() {
        viewModelScope.launch {
            homeRepository.getScrap(
                size = 100,
                sortBy = ScrapSortType.DATE_TIME.name,
                cursor = null,
                requestScrap = RequestScrap(
                    stadiumId = 1,
                    months = monthsSelected.filter { it.isSelected }.map { it.month },
                    good = goodSelected.map { it.name },
                    bad = badSelected.map { it.name }
                )
            ).onSuccess { data ->
                if (data.reviews.isNotEmpty()) {
                    _scrap.value = UiState.Success(data)
                } else {
                    _scrap.value = UiState.Empty
                }
            }.onFailure { e ->
                _scrap.value = UiState.Failure(e.message ?: "실패")
            }
        }
    }

    fun getNextScrapRecord() {
        viewModelScope.launch {
            homeRepository.getScrap(
                size = 100,
                sortBy = ScrapSortType.DATE_TIME.name,
                cursor = (_scrap.value as UiState.Success).data.nextCursor,
                requestScrap = RequestScrap(
                    stadiumId = 1,
                    months = monthsSelected.filter { it.isSelected }.map { it.month },
                    good = goodSelected.map { it.name },
                    bad = badSelected.map { it.name }
                )
            ).onSuccess {
                val updatedList =
                    (scrap.value as UiState.Success).data.reviews.toMutableList().apply {
                        addAll(it.reviews)
                    }
                val updatedScrap = ResponseScrap(
                    reviews = updatedList,
                    nextCursor = it.nextCursor,
                    hasNext = it.hasNext,
                    totalScrapCount = it.totalScrapCount,
                    filter = it.filter
                )
                _scrap.value = UiState.Success(updatedScrap)
            }.onFailure {}
        }
    }

    fun deleteFilter(filterData: FilterNameData) {
        _filter.value = filter.value.filter { it.filterType != filterData.filterType }
        when (filterData.filterType) {
            ScrapFilterType.MONTH -> {
                _months.value = months.value.map { it.copy(isSelected = false) }
                monthsSelected = _months.value
            }

            ScrapFilterType.REVIEW -> {
                _selectedBadReview.value = emptyList()
                _selectedGoodReview.value = emptyList()
                goodSelected = emptyList()
                badSelected = emptyList()
            }

            else -> {}
        }
        getScrapRecord()
    }

    fun setSelectedGoodReview(texts: List<String>) {
        val updatedList: List<GoodReviewData> = texts.map { GoodReviewData(name = it) }
        _selectedGoodReview.value = updatedList
    }

    fun setSelectedBadReview(texts: List<String>) {
        val updatedList: List<BadReviewData> = texts.map { BadReviewData(name = it) }
        _selectedBadReview.value = updatedList
    }

    fun getMonths() {
        if (_months.value.isNotEmpty()) return
        val monthList = (1..12).map { MonthFilterData(it, false) }.toMutableList()
        _months.value = monthList
    }

    fun updateMonth(month: Int) {
        _months.value = months.value.map {
            if (it.month == month) {
                it.copy(isSelected = !it.isSelected)
            } else {
                it
            }
        }
    }

    fun updateAllFilter() {
        monthsSelected = months.value
        goodSelected = selectedGoodReview.value
        badSelected = selectedBadReview.value
        _filter.value = processFilters()
        getScrapRecord()
    }

    fun resetAllFilter() {
        _months.value = monthsSelected
        _selectedGoodReview.value = goodSelected
        _selectedBadReview.value = badSelected
    }

    fun updateLike(id: Int) {
        viewModelScope.launch {
            viewfinderRepository.updateLike(id).onSuccess {
                val currentState = (_scrap.value as? UiState.Success)?.data
                if (currentState != null) {
                    val updatedList = currentState.reviews.map { review ->
                        if (review.baseReview.id == id) {
                            review.copy(
                                baseReview = review.baseReview.copy(
                                    isLiked = !review.baseReview.isLiked,
                                    likesCount = if (review.baseReview.isLiked) {
                                        review.baseReview.likesCount - 1
                                    } else {
                                        review.baseReview.likesCount + 1
                                    }
                                )
                            )
                        } else {
                            review
                        }
                    }

                    _scrap.value = UiState.Success(
                        data = currentState.copy(reviews = updatedList)
                    )
                }
            }.onFailure {
                Timber.d("test 좋아요 업데이트 실패 $it")
            }
        }
    }

    fun updateScrap(id: Int) {
        viewModelScope.launch {
            viewfinderRepository.updateScrap(id).onSuccess {
                val currentState = (_scrap.value as UiState.Success).data
                val updatedList = currentState.reviews.map { review ->
                    if (review.baseReview.id == id) {
                        review.copy(
                            baseReview = review.baseReview.copy(
                                isScrapped = !review.baseReview.isScrapped
                            )
                        )
                    } else {
                        review
                    }
                }
                _scrap.value = UiState.Success(
                    data = currentState.copy(reviews = updatedList)
                )
            }.onFailure {
                Timber.d("test 스크랩 업데이트 실패 $it")
            }
        }

    }


    private fun processFilters(): List<FilterNameData> {
        val updatedList = mutableListOf<FilterNameData>()
        updatedList.add(FilterNameData(ScrapFilterType.STADIUM, "잠실야구장"))
        if (months.value.isNotEmpty()) {
            when (months.value.count { it.isSelected }) {
                0 -> {}
                1 -> updatedList.add(
                    FilterNameData(
                        ScrapFilterType.MONTH,
                        monthsSelected.first { it.isSelected }.formattedMonth()
                    )
                )

                else -> updatedList.add(
                    FilterNameData(
                        ScrapFilterType.MONTH,
                        "${
                            monthsSelected.first { it.isSelected }.formattedMonth()
                        } 외 ${months.value.count { it.isSelected } - 1}개"
                    )
                )
            }
        }
        if (selectedGoodReview.value.isNotEmpty() || selectedBadReview.value.isNotEmpty()) {
            updatedList.add(
                FilterNameData(
                    ScrapFilterType.REVIEW,
                    "키워드 ${selectedGoodReview.value.size + selectedBadReview.value.size}개"
                )
            )
        }
        return updatedList
    }

    enum class ScrapFilterType {
        STADIUM, MONTH, REVIEW
    }

    enum class ScrapSortType {
        DATE_TIME
    }
}

