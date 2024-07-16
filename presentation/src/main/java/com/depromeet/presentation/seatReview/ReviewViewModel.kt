package com.depromeet.presentation.seatReview

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor() : ViewModel() {

    // 날짜 o------
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
    private val currentDate: String = LocalDate.now().format(dateFormatter)

    private val _selectedDate = MutableStateFlow(currentDate)
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    // 시야 후기

    private val _reviewCount = MutableStateFlow(0)
    val reviewCount: StateFlow<Int> = _reviewCount.asStateFlow()

    private val _selectedGoodReview = MutableStateFlow<List<String>>(emptyList())
    val selectedGoodReview: StateFlow<List<String>> = _selectedGoodReview.asStateFlow()

    private val _selectedBadReview = MutableStateFlow<List<String>>(emptyList())
    val selectedBadReview: StateFlow<List<String>> = _selectedBadReview.asStateFlow()

    private val _detailReviewText = MutableStateFlow("")
    val detailReviewText: StateFlow<String> = _detailReviewText.asStateFlow()

    // 좌석 선택

    private val _selectedSeatZone = MutableStateFlow("")
    val selectedSeatZone: StateFlow<String> = _selectedSeatZone.asStateFlow()

    private val _selectedBlock = MutableStateFlow("")
    val selectedBlock: StateFlow<String> = _selectedBlock.asStateFlow()

    private val _selectedColumn = MutableStateFlow("")
    val selectedColumn: StateFlow<String> = _selectedColumn.asStateFlow()

    private val _selectedNumber = MutableStateFlow("")
    val selectedNumber: StateFlow<String> = _selectedNumber.asStateFlow()

    // 날짜
    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
        Log.d("minju", selectedDate.value.toString())
    }

    private val _selectedImages = MutableStateFlow<List<String>>(emptyList())
    val selectedImages: StateFlow<List<String>> = _selectedImages.asStateFlow()

    fun setSelectedImages(image: List<String>) {
        _selectedImages.value = image
        Log.d("minju", selectedImages.value.toString())
    }

    // 시야 후기

    fun setReviewCount(count: Int) {
        _reviewCount.value = count
        Log.d("minju", reviewCount.value.toString())
    }

    fun setSelectedGoodReview(buttonTexts: List<String>) {
        _selectedGoodReview.value = buttonTexts
        Log.d("minju", selectedGoodReview.value.toString())
    }

    fun setSelectedBadReview(buttonTexts: List<String>) {
        _selectedBadReview.value = buttonTexts
        Log.d("minju", selectedBadReview.value.toString())
    }

    fun setDetailReviewText(text: String) {
        _detailReviewText.value = text
        Log.d("minju", detailReviewText.value.toString())
    }

    // 좌석 선택

    fun setSelectedSeatZone(name: String) {
        _selectedSeatZone.value = name
        Log.d("minju", selectedSeatZone.value.toString())
    }

    fun setSelectedBlock(block: String) {
        _selectedBlock.value = block
        Log.d("minju", selectedBlock.value.toString())
    }

    fun setSelectedColumn(column: String) {
        _selectedColumn.value = column
        Log.d("minju", selectedColumn.value.toString())
    }

    fun setSelectedNumber(number: String) {
        _selectedNumber.value = number
        Log.d("minju", selectedNumber.value.toString())
    }
}
