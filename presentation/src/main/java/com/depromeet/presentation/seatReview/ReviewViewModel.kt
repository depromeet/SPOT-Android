package com.depromeet.presentation.seatReview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.request.SeatReviewModel
import com.depromeet.domain.entity.response.seatReview.ResponsePresignedUrlModel
import com.depromeet.domain.entity.response.seatReview.SeatBlockModel
import com.depromeet.domain.entity.response.seatReview.SeatRangeModel
import com.depromeet.domain.entity.response.seatReview.StadiumNameModel
import com.depromeet.domain.entity.response.seatReview.StadiumSectionModel
import com.depromeet.domain.repository.SeatReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
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

    private val _selectedSectionId = MutableStateFlow(0)
    val selectedSectionId: StateFlow<Int> = _selectedSectionId.asStateFlow()

    private val _selectedBlockId = MutableStateFlow(0)
    val selectedBlockId: StateFlow<Int> = _selectedBlockId.asStateFlow()

    private val _stadiumSectionState = MutableStateFlow<UiState<StadiumSectionModel>>(UiState.Empty)
    val stadiumSectionState: StateFlow<UiState<StadiumSectionModel>> = _stadiumSectionState

    private val _seatBlockState = MutableStateFlow<UiState<List<SeatBlockModel>>>(UiState.Empty)
    val seatBlockState: StateFlow<UiState<List<SeatBlockModel>>> = _seatBlockState

    private val _seatRangeState = MutableStateFlow<UiState<List<SeatRangeModel>>>(UiState.Empty)
    val seatRangeState: StateFlow<UiState<List<SeatRangeModel>>> = _seatRangeState

    private val _getPreSignedUrl =
        MutableStateFlow<UiState<ResponsePresignedUrlModel>>(UiState.Loading)
    val getPreSignedUrl = _getPreSignedUrl.asStateFlow()

    private val _postReviewState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val postReviewState: StateFlow<UiState<Unit>> = _postReviewState.asStateFlow()

    fun updateSelectedStadiumId(stadiumId: Int) {
        _selectedStadiumId.value = stadiumId
    }

    fun updateSelectedSectionId(sectionId: Int) {
        _selectedSectionId.value = sectionId
    }

    fun updateSelectedBlockId(blockId: Int) {
        _selectedBlockId.value = blockId
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
                        Timber.e("HTTP error response: ${t.response()?.errorBody()?.string()}")
                        _stadiumNameState.value = UiState.Failure(t.code().toString())
                    } else {
                        Timber.e("General error: ${t.message ?: "Unknown error"}")
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

    fun getSeatBlock(stadiumId: Int, sectionId: Int) {
        viewModelScope.launch {
            _seatBlockState.value = UiState.Loading
            seatReviewRepository.getSeatBlock(stadiumId, sectionId)
                .onSuccess { blocks ->
                    Timber.d("GET BLOCK SUCCESS : $blocks")
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

    fun getSeatRange(stadiumId: Int, sectionId: Int) {
        viewModelScope.launch {
            _seatRangeState.value = UiState.Loading
            seatReviewRepository.getSeatRange(
                stadiumId,
                sectionId,
            ).onSuccess { range ->
                Timber.d("GET RANGE SUCCESS : $range")
                if (range.isEmpty()) {
                    _seatRangeState.value = UiState.Empty
                } else {
                    _seatRangeState.value = UiState.Success(range)
                }
            }.onFailure { t ->
                if (t is HttpException) {
                    Timber.e("GET RANGE FAILURE : $t")
                    _seatRangeState.value = UiState.Failure(t.code().toString())
                }
            }
        }
    }

    // presigned URL 요청
    fun requestPreSignedUrl(fileExtension: String) {
        viewModelScope.launch {
            _getPreSignedUrl.value = UiState.Loading
            seatReviewRepository.postReviewImagePresigned(fileExtension)
                .onSuccess { response ->
                    Timber.d("REQUEST PRESIGNED URL SUCCESS : $response")
                    _getPreSignedUrl.value = UiState.Success(response)
                }
                .onFailure { t ->
                    Timber.e("REQUEST PRESIGNED URL FAILURE : $t")
                    val errorMessage = if (t is HttpException) {
                        val errorBody = t.response()?.errorBody()?.string()
                        "HTTP Error ${t.code()}: ${errorBody ?: "Unknown error"}"
                    } else {
                        t.message ?: "Unknown error"
                    }

                    _getPreSignedUrl.value = UiState.Failure(errorMessage)
                }
        }
    }

    // 이미지 업로드
    fun uploadImageToPreSignedUrl(
        presignedUrl: String,
        image: ByteArray,
    ): CompletableDeferred<Boolean> {
        val deferred = CompletableDeferred<Boolean>()
        viewModelScope.launch {
            val result = seatReviewRepository.putImagePreSignedUrl(presignedUrl, image)
            result.onSuccess {
                Timber.d("UPLOAD IMAGE SUCCESS")
                deferred.complete(true)
            }.onFailure { t ->
                Timber.e("UPLOAD IMAGE FAILURE : $t")
                if (t is HttpException) {
                    Timber.e("HTTP error code: ${t.code()}")
                    Timber.e("HTTP error response: ${t.response()?.errorBody()?.string()}")
                } else {
                    Timber.e("General error: ${t.message ?: "Unknown error"}")
                }
                deferred.complete(false)
            }
        }
        return deferred
    }

    fun postSeatReview() {
        viewModelScope.launch {
            val seatReviewModel = SeatReviewModel(
                images = _selectedImages.value,
                dateTime = _selectedDate.value,
                good = _selectedGoodReview.value,
                bad = _selectedBadReview.value,
                content = _detailReviewText.value,
            )

            Timber.d("Selected Images: ${_selectedImages.value}")
            Timber.d("Selected Date: ${_selectedDate.value}")
            Timber.d("Good Review: ${_selectedGoodReview.value}")
            Timber.d("Bad Review: ${_selectedBadReview.value}")
            Timber.d("Detail Review Text: ${_detailReviewText.value}")
            Timber.d("Selected Stadium ID: ${_selectedStadiumId.value}")
            Timber.d("Selected Block ID: ${_selectedBlockId.value}")
            Timber.d("Selected seatNumber: ${selectedNumber.value}")

            val selectedNumberValue = selectedNumber.value
            if (selectedNumberValue.isNullOrEmpty()) {
                Timber.e("Selected Number is null or empty")
                _postReviewState.value = UiState.Failure("Selected Number is required")
                return@launch
            }

            val selectedNumberInt = try {
                selectedNumberValue.toInt()
            } catch (e: NumberFormatException) {
                Timber.e("Selected Number is not a valid integer: $selectedNumberValue")
                _postReviewState.value = UiState.Failure("Selected Number is not a valid integer")
                return@launch
            }

            Timber.d("Selected Number: $selectedNumberInt")

            _postReviewState.value = UiState.Loading

            seatReviewRepository.postSeatReview(
                _selectedBlockId.value,
                selectedNumberInt,
                seatReviewModel,
            )
                .onSuccess {
                    _postReviewState.value = UiState.Success(Unit)
                    Timber.d("POST REVIEW SUCCESS")
                }
                .onFailure { t ->
                    Timber.e("POST REVIEW FAILURE : $t")

                    if (t is HttpException) {
                        val errorBody = t.response()?.errorBody()?.string()
                        Timber.e("Error Body: $errorBody")

                        val errorMessage = when {
                            t.code() == 403 -> "권한이 없습니다. 요청을 확인하고 다시 시도해 주세요."
                            errorBody.isNullOrEmpty() -> "HTTP ${t.code()} 에러 발생: ${t.message()}"
                            else -> errorBody
                        }

                        _postReviewState.value = UiState.Failure(errorMessage)
                    } else {
                        _postReviewState.value = UiState.Failure(t.message ?: "알 수 없는 오류")
                    }
                }
        }
    }

}
