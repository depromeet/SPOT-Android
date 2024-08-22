package com.dpm.presentation.scrap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ScrapData(
    val id: Int,
    val image: String,
    val stadiumName: String,
    val seatName: String,
    val isBookmark: Boolean,
)

fun getScrapData(): List<ScrapData> {
    return listOf(
        ScrapData(
            id = 1,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 네이비석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapData(
            id = 2,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 레드석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapData(
            id = 3,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 외야그린석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapData(
            id = 4,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 오렌지석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapData(
            id = 5,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 블루석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapData(
            id = 6,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 네이비석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapData(
            id = 7,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 레드석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapData(
            id = 8,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 외야그린석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapData(
            id = 9,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 오렌지석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapData(
            id = 10,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 블루석 302블록 1열 1변",
            isBookmark = true
        ),
    )
}

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
class ScrapViewModel @Inject constructor() : ViewModel() {

    private var monthsSelected: List<MonthFilterData> = emptyList()
    private var goodSelected: List<GoodReviewData> = emptyList()
    private var badSelected: List<BadReviewData> = emptyList()


    private val _scrap = MutableStateFlow<UiState<List<ScrapData>>>(UiState.Loading)
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
//            _scrap.value = UiState.Failure("실패")
//            _scrap.value = UiState.Empty
            _scrap.value = UiState.Success(getScrapData())
        }
    }

    fun deleteScrapRecord(id: Int) {
        //TODO : 서버통신
        val currentState = _scrap.value
        if (currentState is UiState.Success) {
            val scrapList: List<ScrapData> = currentState.data
            val updatedList = scrapList.filter { it.id != id }
            _scrap.value = UiState.Success(updatedList)
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
        if(_months.value.isNotEmpty()) return
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
    }

    fun resetAllFilter() {
        _months.value = monthsSelected
        _selectedGoodReview.value = goodSelected
        _selectedBadReview.value = badSelected
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
                        months.value.first().formattedMonth()
                    )
                )

                else -> updatedList.add(
                    FilterNameData(
                        ScrapFilterType.MONTH,
                        "${
                            months.value.first().formattedMonth()
                        } 외 ${months.value.count { it.isSelected }}개"
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

}

