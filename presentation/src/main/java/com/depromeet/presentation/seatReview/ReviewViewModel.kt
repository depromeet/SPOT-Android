package com.depromeet.presentation.seatReview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.seatReview.SeatBlockModel
import com.depromeet.domain.entity.response.seatReview.StadiumNameModel
import com.depromeet.domain.entity.response.seatReview.StadiumSectionModel
import com.depromeet.domain.repository.SeatReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val seatReviewRepository: SeatReviewRepository,
) : ViewModel() {

    // 날짜 및 이미지
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
    private val currentDate: String = LocalDate.now().format(dateFormatter)

    private val _selectedDate = MutableStateFlow(currentDate)
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedImages = MutableStateFlow<List<String>>(emptyList())
    val selectedImages: StateFlow<List<String>> = _selectedImages.asStateFlow()

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

    // 서버 통신

    private val _stadiumNameState = MutableStateFlow<UiState<List<StadiumNameModel>>>(UiState.Empty)
    val stadiumNameState: StateFlow<UiState<List<StadiumNameModel>>> = _stadiumNameState

    private val _selectedStadiumId = MutableStateFlow(0)
    val selectedStadiumId: StateFlow<Int> = _selectedStadiumId.asStateFlow()

    private val _stadiumSectionState = MutableStateFlow<UiState<StadiumSectionModel>>(UiState.Empty)
    val stadiumSectionState: StateFlow<UiState<StadiumSectionModel>> = _stadiumSectionState

    private val _seatBlockState = MutableStateFlow<UiState<List<SeatBlockModel>>>(UiState.Empty)
    val seatBlockState: StateFlow<UiState<List<SeatBlockModel>>> = _seatBlockState

    fun setSelectedStadiumId(stadiumId: Int) {
        _selectedStadiumId.value = stadiumId
    }

    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun setSelectedImages(image: List<String>) {
        _selectedImages.value = image
    }

    fun setReviewCount(count: Int) {
        _reviewCount.value = count
    }

    fun setSelectedGoodReview(buttonTexts: List<String>) {
        _selectedGoodReview.value = buttonTexts
    }

    fun setSelectedBadReview(buttonTexts: List<String>) {
        _selectedBadReview.value = buttonTexts
    }

    fun setDetailReviewText(text: String) {
        _detailReviewText.value = text
    }

    fun setSelectedSeatZone(name: String) {
        _selectedSeatZone.value = name
    }

    fun setSelectedBlock(block: String) {
        _selectedBlock.value = block
    }

    fun setSelectedColumn(column: String) {
        _selectedColumn.value = column
    }

    fun setSelectedNumber(number: String) {
        _selectedNumber.value = number
    }

    fun getStadiumName() {
        viewModelScope.launch {
            _stadiumNameState.value = UiState.Loading
            seatReviewRepository.getStadiumName()
                .onSuccess { stadium ->
                    Timber.d("GET NAME SUCCESS : $stadium")
                    if (stadium.isEmpty()) {
                        _stadiumNameState.value = UiState.Empty
                    } else {
                        _stadiumNameState.value = UiState.Success(stadium)
                    }
                }
                .onFailure { t ->
                    Timber.e("GET NAME FAILURE : ${t.message}", t)
                    if (t is HttpException) {
                        Timber.e("HTTP error code: ${t.code()}")
                        _stadiumNameState.value = UiState.Failure(t.code().toString())
                    } else {
                        Timber.e("General error: ${t.message ?: "Unknown error"}")
                    }
                }
        }
    }

    fun getSeatBlock(stadiumId: Int, sectionId: Int) {
        viewModelScope.launch {
            _seatBlockState.value = UiState.Loading
            seatReviewRepository.getSeatBlock(stadiumId, sectionId)
                .onSuccess { blocks ->
                    Timber.e("GET BLOCK FAILURE : $blocks")
                    if (blocks.isEmpty()) {
                        _seatBlockState.value = UiState.Empty
                    } else {
                        _seatBlockState.value = UiState.Success(blocks)
                    }
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("GET BLOCK FAILURE : $t")
                        _seatBlockState.value = UiState.Failure(t.code().toString())
                    }
                }
        }
    }

    fun getStadiumSection(stadiumId: Int) {
        viewModelScope.launch {
            _stadiumSectionState.value = UiState.Loading
            seatReviewRepository.getStadiumSection(
                stadiumId,
            ).onSuccess { section ->
                Timber.d("GET SECTION SUCCESS : $section")
                if (section == null) {
                    _stadiumSectionState.value = UiState.Empty
                    return@launch
                }
                _stadiumSectionState.value = when {
                    section.sectionList.isEmpty() -> UiState.Empty
                    else -> UiState.Success(section)
                }
            }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("GET SECTION FAILURE : $t")
                        _stadiumSectionState.value = UiState.Failure(t.code().toString())
                    }
                }
        }
    }
}
