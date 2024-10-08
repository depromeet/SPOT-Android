package com.dpm.presentation.seatreview.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import com.dpm.domain.entity.request.seatreview.RequestSeatReview
import com.dpm.domain.entity.response.seatreview.ResponsePresignedUrl
import com.dpm.domain.entity.response.seatreview.ResponseSeatBlock
import com.dpm.domain.entity.response.seatreview.ResponseSeatRange
import com.dpm.domain.entity.response.seatreview.ResponseSeatReview
import com.dpm.domain.entity.response.seatreview.ResponseStadiumName
import com.dpm.domain.entity.response.seatreview.ResponseStadiumSection
import com.dpm.domain.model.seatreview.ReviewMethod
import com.dpm.domain.model.seatreview.ValidSeat
import com.dpm.domain.repository.SeatReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val seatReviewRepository: SeatReviewRepository,
) : ViewModel() {

    // 날짜
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val currentDate: String = LocalDateTime.now().format(dateFormatter)

    private val _selectedDate = MutableStateFlow(currentDate)
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    // 이미지

    private val _selectedImages = MutableStateFlow<List<String>>(emptyList())
    val selectedImages: StateFlow<List<String>> = _selectedImages.asStateFlow()

    private val _preSignedUrlImages = MutableStateFlow<List<String>>(emptyList())
    val preSignedUrlImages: StateFlow<List<String>> = _preSignedUrlImages.asStateFlow()

    private val _selectViewImageUrl = MutableStateFlow<List<String>>(emptyList())
    val selectViewImageUrl: StateFlow<List<String>> = _selectViewImageUrl

    fun toggleImageSelection(imageUri: String) {
        _selectViewImageUrl.value = _selectViewImageUrl.value.toMutableList().apply {
            if (contains(imageUri)) {
                remove(imageUri)
            } else {
                add(imageUri)
            }
        }
    }

    fun updateViewReview(
        selectedColumn: String,
        selectedNumber: String,
        preSignedUrlImages: List<String>,
        selectedGoodReview: List<String>,
        selectedBadReview: List<String>,
        detailReviewText: String,
        selectedDate: String,
        blockId: Int,
    ) {
        _selectedColumn.value = selectedColumn
        _selectedNumber.value = selectedNumber
        _preSignedUrlImages.value = preSignedUrlImages
        _selectedGoodReview.value = selectedGoodReview
        _selectedBadReview.value = selectedBadReview
        _detailReviewText.value = detailReviewText
        _selectedDate.value = selectedDate
        _selectedBlockId.value = blockId
    }

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

    val userSeatState = MutableLiveData<ValidSeat>()

    private val _sectionItemSelected = MutableStateFlow(false)
    val sectionItemSelected: StateFlow<Boolean> = _sectionItemSelected

    fun updateItemSelected(isSelected: Boolean) {
        _sectionItemSelected.value = isSelected
    }

    // 서버 통신

    private val _stadiumNameState = MutableStateFlow<UiState<List<ResponseStadiumName>>>(UiState.Empty)
    val stadiumNameState: StateFlow<UiState<List<ResponseStadiumName>>> = _stadiumNameState

    private val _selectedStadiumId = MutableStateFlow(0)
    val selectedStadiumId: StateFlow<Int> = _selectedStadiumId.asStateFlow()

    private val _selectedSectionId = MutableStateFlow(0)
    val selectedSectionId: StateFlow<Int> = _selectedSectionId.asStateFlow()

    private val _selectedBlockId = MutableStateFlow(0)
    val selectedBlockId: StateFlow<Int> = _selectedBlockId.asStateFlow()

    private val _stadiumSectionState = MutableStateFlow<UiState<ResponseStadiumSection>>(UiState.Empty)
    val stadiumSectionState: StateFlow<UiState<ResponseStadiumSection>> = _stadiumSectionState

    private val _seatBlockState = MutableStateFlow<UiState<List<ResponseSeatBlock>>>(UiState.Empty)
    val seatBlockState: StateFlow<UiState<List<ResponseSeatBlock>>> = _seatBlockState

    private val _seatRangeState = MutableStateFlow<UiState<List<ResponseSeatRange>>>(UiState.Empty)
    val seatRangeState: StateFlow<UiState<List<ResponseSeatRange>>> = _seatRangeState

    private val _getPreSignedUrl =
        MutableStateFlow<UiState<ResponsePresignedUrl>>(UiState.Loading)
    val getPreSignedUrl = _getPreSignedUrl.asStateFlow()

    private val _postReviewState = MutableStateFlow<UiState<ResponseSeatReview>>(UiState.Empty)
    val postReviewState: StateFlow<UiState<ResponseSeatReview>> = _postReviewState.asStateFlow()

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

    fun setPreSignedUrlImages(images: List<String>) {
        val newImages = images.map { removeQueryParameters(it) }.toSet()
        val currentImages = _preSignedUrlImages.value.map { removeQueryParameters(it) }.toSet()
        val updatedImages = (currentImages + newImages).toList()
        _preSignedUrlImages.value = updatedImages
    }

    private fun removeQueryParameters(url: String): String {
        val uri = Uri.parse(url)
        return uri.buildUpon().clearQuery().build().toString()
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

    fun getBlockListName(blockCode: String): String {
        return when {
            selectedSectionId.value == 10 && blockCode.endsWith("w") -> {
                when (val codeWithoutW = blockCode.removeSuffix("w")) {
                    "101", "102", "121", "122" -> "레드-$codeWithoutW"
                    "109", "114" -> "블루-$codeWithoutW"
                    else -> codeWithoutW
                }
            }
            selectedSectionId.value == 8 && blockCode.startsWith("exciting") -> {
                when (blockCode) {
                    "exciting1" -> "1루 익사이팅석"
                    "exciting3" -> "3루 익사이팅석"
                    else -> blockCode
                }
            }

            selectedSectionId.value == 1 && blockCode.startsWith("premium") -> {
                when (blockCode) {
                    "premium" -> "프리미엄석"
                    else -> blockCode
                }
            }
            else -> blockCode
        }
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

    fun requestPreSignedUrl(fileExtension: String) {
        viewModelScope.launch {
            _getPreSignedUrl.value = UiState.Loading
            seatReviewRepository.postReviewImagePresigned(fileExtension)
                .onSuccess { response ->
                    Timber.d("REQUEST PRESIGNED URL SUCCESS : $response")
                    _getPreSignedUrl.value = UiState.Success(response)
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("REQUEST PRESIGNED URL FAILURE : $t")
                        _getPreSignedUrl.value = UiState.Failure(t.code().toString())
                    }
                }
        }
    }

    private var index = -1
    private var _count = MutableStateFlow(0)
    val count = _count.asStateFlow()

    private fun uploadImageToPreSignedUrl(
        presignedUrl: String,
        imageDataList: List<ByteArray>,
    ) {
        index++
        viewModelScope.launch {
            val result =
                seatReviewRepository.putImagePreSignedUrl(presignedUrl, imageDataList[index])
            result.onSuccess {
                _count.value += 1
            }.onFailure { t ->
                if (t is HttpException) {
                    Timber.e("UPLOAD IMAGE FAILURE for presignedUrl: $presignedUrl with error: $t")
                }
            }
        }
    }

    fun postSeatReview(reviewType: ReviewMethod) {
        viewModelScope.launch {
            val requestSeatReview = RequestSeatReview(
                rowNumber = _selectedColumn.value.toIntOrNull(),
                seatNumber = _selectedNumber.value.toIntOrNull(),
                images = _preSignedUrlImages.value,
                good = _selectedGoodReview.value,
                bad = _selectedBadReview.value,
                content = detailReviewText.value,
                dateTime = _selectedDate.value,
                reviewType = reviewType.toString(),

            )
            Timber.d("Selected Images: ${_preSignedUrlImages.value}")
            Timber.d("Selected Date: ${_selectedDate.value}")
            Timber.d("Good Review: ${_selectedGoodReview.value}")
            Timber.d("Bad Review: ${_selectedBadReview.value}")
            Timber.d("Detail Review Text: ${detailReviewText.value}")
            Timber.d("Selected Stadium ID: ${_selectedStadiumId.value}")
            Timber.d("Selected Block ID: ${_selectedBlockId.value}")
            Timber.d("Selected seatColumn: ${selectedColumn.value}")
            Timber.d("Selected seatNumber: ${selectedNumber.value}")
            Timber.d("Selected reviewType: $reviewType")
            _postReviewState.value = UiState.Loading

            try {
                seatReviewRepository.postSeatReview(
                    _selectedBlockId.value,
                    requestSeatReview,
                ).onSuccess { id ->
                    _postReviewState.value = UiState.Success(id)
                    Log.d("minju3", "뷰모델 성공")
                    Timber.d("POST REVIEW SUCCESS")
                }.onFailure { t ->
                    Log.d("minju3", "뷰모델 실패")
                    Timber.e(t, "POST REVIEW FAILURE: ${t.message}")
                }
            } catch (e: Exception) {
                Log.d("minju3", "뷰모델 예외 발생")
                Timber.e(e, "Exception in postSeatReview")
            }
        }
    }

    fun uploadImagesSequentially(
        presignedUrl: String,
        imageDataList: List<ByteArray>,
    ) {
        uploadImageToPreSignedUrl(
            presignedUrl,
            imageDataList,
        )
    }

    private val _reviewMethod = MutableLiveData<ReviewMethod>()
    val reviewMethod: LiveData<ReviewMethod> get() = _reviewMethod

    fun setReviewMethod(method: ReviewMethod) {
        _reviewMethod.value = method
    }
}
