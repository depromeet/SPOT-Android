package com.dpm.presentation.scrap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ScrapTestData(
    val id: Int,
    val image: String,
    val stadiumName: String,
    val seatName: String,
    val isBookmark: Boolean,
)

fun getScrapData(): List<ScrapTestData> {
    return listOf(
        ScrapTestData(
            id = 1,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 네이비석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapTestData(
            id = 2,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 레드석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapTestData(
            id = 3,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 외야그린석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapTestData(
            id = 4,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 오렌지석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapTestData(
            id = 5,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 블루석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapTestData(
            id = 6,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 네이비석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapTestData(
            id = 7,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 레드석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapTestData(
            id = 8,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 외야그린석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapTestData(
            id = 9,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 오렌지석 302블록 1열 1변",
            isBookmark = true
        ),
        ScrapTestData(
            id = 10,
            image = "https://picsum.photos/600/400",
            stadiumName = "잠실 야구장",
            seatName = "1루 블루석 302블록 1열 1변",
            isBookmark = true
        ),
    )
}

data class FilterTestData(
    val name: String,
)


data class ScrapMonth(
    val month: Int,
    val isSelected: Boolean,
) {
    fun formattedMonth(): String {
        return "${month}월"
    }
}


@HiltViewModel
class ScrapViewModel @Inject constructor() : ViewModel() {

    private val _scrap = MutableStateFlow<UiState<List<ScrapTestData>>>(UiState.Loading)
    val scrap = _scrap.asStateFlow()

    val filter = MutableStateFlow<List<FilterTestData>>(emptyList())

    private val _months = MutableStateFlow<List<ScrapMonth>>(emptyList())
    val months = _months.asStateFlow()

//    val goods = MutableStateFlow<List<Int>>(emptyList())
//    val bads = MutableStateFlow<List<Int>>(emptyList())


    fun getScrapRecord() {
        viewModelScope.launch {
//            _scrap.value = UiState.Failure("실패")
//            _scrap.value = UiState.Empty
            _scrap.value = UiState.Success(getScrapData())
        }
    }

    fun deleteScrapRecord(id: Int) {
        val currentState = _scrap.value
        if (currentState is UiState.Success) {
            val scrapList: List<ScrapTestData> = currentState.data
            val updatedList = scrapList.filter { it.id != id }
            _scrap.value = UiState.Success(updatedList)
        }
    }

    fun getMonths() {
        val monthList = (1..12).map { ScrapMonth(it, false) }.toMutableList()
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


}