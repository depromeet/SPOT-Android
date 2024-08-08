package com.depromeet.presentation.seatreview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.webkit.MimeTypeMap
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityReviewBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.HomeActivity
import com.depromeet.presentation.seatreview.dialog.DatePickerDialog
import com.depromeet.presentation.seatreview.dialog.ImageUploadDialog
import com.depromeet.presentation.seatreview.dialog.ReviewMySeatDialog
import com.depromeet.presentation.seatreview.dialog.SelectSeatDialog
import com.depromeet.presentation.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileNotFoundException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class ReviewActivity : BaseActivity<ActivityReviewBinding>({
    ActivityReviewBinding.inflate(it)
}) {
    companion object {
        private const val DATE_FORMAT = "yyyy.MM.dd"
        private const val ISO_DATE_FORMAT = "yyyy-MM-dd HH:mm"
        private const val FRAGMENT_RESULT_KEY = "requestKey"
        private const val SELECTED_IMAGES = "selected_images"
        private const val MAX_SELECTED_IMAGES = 3
    }

    private val viewModel by viewModels<ReviewViewModel>()
    private val selectedImage: List<ImageView> by lazy { listOf(binding.ivFirstImage, binding.ivSecondImage, binding.ivThirdImage) }
    private val selectedImageLayout: List<FrameLayout> by lazy { listOf(binding.layoutFirstImage, binding.layoutSecondImage, binding.layoutThirdImage) }
    private val removeButtons: List<ImageView> by lazy { listOf(binding.ivRemoveFirstImage, binding.ivRemoveSecondImage, binding.ivRemoveThirdImage) }
    private var selectedImageUris: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewStatusBar()
        setupFragmentResultListener()

        viewModel.getStadiumName()
        initObserveStadiumName()
        initObservePreSignedUrl()
        initObserveUploadImageToS3()
        initObserveUploadReview()

        initDatePickerDialog()
        initUploadDialog()
        initSeatReviewDialog()

        initEventUploadBtn()
        initEventRemoveBtn()
        initEventToHome()
    }

    private fun initViewStatusBar() {
        Utils(this).apply {
            setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_tertiary)
            setBlackSystemBarIconColor(window)
        }
    }

    private fun observeReviewViewModel() {
        viewModel.selectedImages.asLiveData().observe(this) { image ->
            updateNextButtonState()
        }

        viewModel.reviewCount.asLiveData().observe(this) { count ->
            binding.tvMySeatReviewCount.text = "${count}개"
            binding.layoutReviewNumber.visibility = if (count > 0) View.VISIBLE else View.GONE
        }

        viewModel.selectedGoodReview.asLiveData().observe(this) { count ->
            updateNextButtonState()
        }

        viewModel.selectedBadReview.asLiveData().observe(this) { count ->
            updateNextButtonState()
        }

        viewModel.selectedSeatZone.asLiveData().observe(this) { name ->
            val formattedName = name.toString().replace("\n", "").replace("\r", "")
            binding.tvSeatColor.text = formattedName
            updateLayoutSeatInfoVisibility()
            updateNextButtonState()
        }

        viewModel.selectedBlock.asLiveData().observe(this) { block ->
            binding.tvSeatBlock.text = block.toString()
            updateLayoutSeatInfoVisibility()
            updateNextButtonState()
        }

        viewModel.selectedColumn.asLiveData().observe(this) { column ->
            binding.tvColumnNumber.text = column.toString()
            updateLayoutSeatInfoVisibility()
        }

        viewModel.selectedNumber.asLiveData().observe(this) { number ->
            binding.tvSeatNumber.text = "${number}번"
            updateLayoutSeatInfoVisibility()
            updateNextButtonState()
        }
    }

    private fun initDatePickerDialog() {
        binding.layoutDatePicker.setOnSingleClickListener {
            DatePickerDialog().show(supportFragmentManager, "DatePickerDialogTag")
        }
        viewModel.selectedDate.asLiveData().observe(this) { date ->
            val originalFormat = SimpleDateFormat(ISO_DATE_FORMAT, Locale.getDefault())
            val targetFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            val dateOnly = originalFormat.parse(date)?.let { targetFormat.format(it) }
            binding.tvDate.text = dateOnly ?: date.substring(0, 10)
            updateNextButtonState()
        }
    }

    private fun initUploadDialog() {
        binding.btnAddImage.setOnClickListener {
            ImageUploadDialog().show(supportFragmentManager, "ImageUploadDialog")
        }
    }

    private fun initEventToHome() {
        binding.btnBack.setOnSingleClickListener {
            Intent(this, HomeActivity::class.java).apply { startActivity(this) }
        }
    }

    private fun initObserveStadiumName() {
        viewModel.stadiumNameState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    val firstStadium = state.data.firstOrNull()
                    if (firstStadium != null) {
                        viewModel.getStadiumSection(firstStadium.id)
                        viewModel.updateSelectedStadiumId(firstStadium.id)
                    }
                    observeReviewViewModel()
                }
                is UiState.Failure -> { toast("오류가 발생했습니다") }
                is UiState.Loading -> {}
                is UiState.Empty -> {}
                else -> {}
            }
        }
    }

    private fun initSeatReviewDialog() {
        binding.layoutReviewMySeat.setOnSingleClickListener {
            ReviewMySeatDialog().show(supportFragmentManager, "ReviewMySeatDialog")
        }
        binding.layoutSeatInfoNext.setOnSingleClickListener {
            SelectSeatDialog().show(supportFragmentManager, "SelectSeatDialog")
        }
    }
    private fun setupFragmentResultListener() {
        supportFragmentManager.setFragmentResultListener(FRAGMENT_RESULT_KEY, this) { _, bundle ->
            val newSelectedImages = bundle.getStringArrayList(SELECTED_IMAGES)
            newSelectedImages?.let { addSelectedImages(it) }
        }
    }

    private fun addSelectedImages(newImageUris: List<String>) {
        selectedImageUris.addAll(newImageUris.filterNot { selectedImageUris.contains(it) })
        if (selectedImageUris.size > MAX_SELECTED_IMAGES) {
            selectedImageUris = selectedImageUris.take(MAX_SELECTED_IMAGES).toMutableList()
        }
        viewModel.setSelectedImages(selectedImageUris)
        updateSelectedImages()
    }

    private fun initEventRemoveBtn() {
        removeButtons.forEachIndexed { index, button ->
            button.setOnSingleClickListener {
                removeImageAt(index)
            }
        }
    }

    private fun removeImageAt(index: Int) {
        if (index < selectedImageUris.size) {
            selectedImageUris.removeAt(index)
            updateSelectedImages()
            viewModel.setSelectedImages(selectedImageUris)
        }
    }

    private fun updateLayoutSeatInfoVisibility() {
        val seatName = viewModel.selectedSeatZone.value
        val block = viewModel.selectedBlock.value
        val column = viewModel.selectedColumn.value
        val number = viewModel.selectedNumber.value
        if (listOf(seatName, block, column, number).any { it.isNullOrEmpty() }) {
            binding.layoutSeatInfo.visibility = INVISIBLE
        } else {
            binding.layoutSeatInfo.visibility = VISIBLE
        }
    }

    private fun updateSelectedImages() {
        with(binding) {
            layoutAddDefaultImage.isVisible = selectedImageUris.isEmpty()
            selectedImageUris.forEachIndexed { index, uri ->
                if (index < selectedImage.size && index < selectedImageLayout.size) {
                    val image = selectedImage[index]
                    val layout = selectedImageLayout[index]
                    layout.isVisible = true
                    image.setImageURI(Uri.parse(uri))
                    image.load(Uri.parse(uri)) {
                        transformations(RoundedCornersTransformation(26f))
                    }
                    removeButtons[index].isVisible = true
                }
            }
            for (index in selectedImageUris.size until selectedImage.size) {
                val layout = selectedImageLayout[index]
                layout.isVisible = false
                removeButtons[index].isVisible = false
            }
            if (selectedImageUris.size == MAX_SELECTED_IMAGES) {
                svAddImage.post { svAddImage.fullScroll(View.FOCUS_RIGHT) }
            }
            layoutAddImageButton.isVisible = selectedImageUris.size < selectedImage.size
            tvImageCount.text = selectedImageUris.size.toString()
        }
    }
    private fun updateNextButtonState() {
        val isSelectedDateFilled = viewModel.selectedDate.value.isNotEmpty()
        val isSelectedImageFilled = viewModel.selectedImages.value.isNotEmpty()
        val isSelectedGoodBtnFilled = viewModel.selectedGoodReview.value.isNotEmpty()
        val isSelectedBadBtnFilled = viewModel.selectedBadReview.value.isNotEmpty()
        val isSelectedBlockFilled = viewModel.selectedBlock.value.isNotEmpty()
        val isSelectedNumberFilled = viewModel.selectedNumber.value.isNotEmpty()

        with(binding.tvUploadBtn) {
            isEnabled =
                isSelectedDateFilled && isSelectedImageFilled && (isSelectedGoodBtnFilled || isSelectedBadBtnFilled) &&
                isSelectedBlockFilled && isSelectedNumberFilled
            if (isEnabled) {
                setBackgroundResource(R.drawable.rect_action_enabled_fill_8)
            } else {
                setBackgroundResource(R.drawable.rect_action_disabled_fill_8)
            }
        }
    }

    private fun getFileExtension(context: Context, uri: Uri): String {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) } ?: ""
    }

    private fun getFileInputStream(context: Context, uri: Uri): InputStream? {
        return try {
            context.contentResolver.openInputStream(uri)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    private fun readImageData(context: Context, uri: Uri): ByteArray? {
        val inputStream = getFileInputStream(context, uri)
        return inputStream?.use { it.readBytes() }
    }

    private fun initEventUploadBtn() {
        binding.tvUploadBtn.setOnSingleClickListener {
            val uniqueImageUris = selectedImageUris.distinct()
            uniqueImageUris.forEach { imageUriString ->
                val imageUri = Uri.parse(imageUriString)
                val fileExtension = getFileExtension(this, imageUri)
                val imageData = readImageData(this, imageUri)
                if (imageData != null) {
                    viewModel.requestPreSignedUrl(fileExtension)
                } else {
                    toast("파일을 읽을 수 없습니다.")
                }
            }
        }
    }

    private fun initObservePreSignedUrl() {
        viewModel.getPreSignedUrl.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    val presignedUrl = state.data.presignedUrl
                    val imageDataList = selectedImageUris.mapNotNull { imageUriString ->
                        val imageUri = Uri.parse(imageUriString)
                        val imageData = readImageData(this, imageUri)
                        imageData
                    }
                    if (viewModel.preSignedUrlImages.value.contains(presignedUrl).not()) {
                        viewModel.setPreSignedUrlImages(listOf(presignedUrl))
                    }
                    viewModel.uploadImagesSequentially(presignedUrl, imageDataList)
                }

                is UiState.Failure -> {
                    toast("Presigned URL 요청 실패")
                }

                else -> {}
            }
        }
    }

    private fun initObserveUploadImageToS3() {
        viewModel.count.asLiveData().observe(this) {
            if (it == selectedImageUris.size && it != 0) {
                viewModel.postSeatReview()
            }
        }
    }

    private fun initObserveUploadReview() {
        viewModel.postReviewState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    Intent(this, ReviewDoneActivity::class.java).apply {
                        startActivity(this)
                    }
                }

                is UiState.Failure -> {
                    toast("리뷰 등록 실패: $state")
                }
                else -> {}
            }
        }
    }
}
