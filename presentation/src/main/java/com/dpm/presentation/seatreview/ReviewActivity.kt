package com.dpm.presentation.seatreview

import ReviewData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.MimeTypeMap
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityReviewBinding
import com.dpm.core.base.BaseActivity
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotImageSnackBar
import com.dpm.domain.model.seatreview.ReviewMethod
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.extension.toast
import com.dpm.presentation.home.HomeActivity
import com.dpm.presentation.seatreview.dialog.main.DatePickerDialog
import com.dpm.presentation.seatreview.dialog.main.ImageUploadDialog
import com.dpm.presentation.seatreview.dialog.main.ReviewMySeatDialog
import com.dpm.presentation.seatreview.dialog.main.SelectSeatDialog
import com.dpm.presentation.seatreview.viewmodel.ReviewViewModel
import com.dpm.presentation.util.Utils
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
        private const val REVIEW_MY_SEAT_DIALOG = "ReviewMySeatDialog"
        private const val SELECT_SEAT_DIALOG = "SelectSeatDialog"
        private const val DATE_PICKER_DIALOG_TAG = "DatePickerDialogTag"
        private const val IMAGE_UPLOAD_DIALOG = "ImageUploadDialog"
    }

    private val viewModel by viewModels<ReviewViewModel>()
    private val method by lazy { intent.getStringExtra("METHOD_KEY")?.let { ReviewMethod.valueOf(it) } }
    private val selectedImage: List<ImageView> by lazy {
        listOf(
            binding.ivFirstImage,
            binding.ivSecondImage,
            binding.ivThirdImage,
        )
    }
    private val selectedImageLayout: List<FrameLayout> by lazy {
        listOf(
            binding.layoutFirstImage,
            binding.layoutSecondImage,
            binding.layoutThirdImage,
        )
    }
    private val removeButtons: List<ImageView> by lazy {
        listOf(
            binding.ivRemoveFirstImage,
            binding.ivRemoveSecondImage,
            binding.ivRemoveThirdImage,
        )
    }
    private var selectedImageUris: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserve()
        initEvent()
    }

    private fun initView() {
        initMethodNaming()
        viewModel.getStadiumName()
        initDatePickerDialog()
        initUploadDialog()
        initSeatReviewDialog()
        initViewStatusBar()
        initSeatInfoView()
    }

    private fun initEvent() {
        initEventUploadBtn()
        initEventRemoveBtn()
        initEventToHome()
    }

    private fun initObserve() {
        observeStadiumName()
        observePreSignedUrl()
        observeUploadImageToS3()
        observeUploadReview()
    }

    private fun initViewStatusBar() {
        Utils(this).apply {
            setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_tertiary)
            setBlackSystemBarIconColor(window)
        }
    }

    private fun initMethodNaming() {
        when (method) {
            ReviewMethod.VIEW -> {
                binding.tvTitle.text = "좌석의 시야를 공유해보세요"
                binding.tvAddImage.text = "야구장 시야 사진을\n올려주세요"
                binding.tvReviewMySeat.text = "내 시야 후기"
            }
            ReviewMethod.FEED -> {
                binding.tvTitle.text = "경기의 순간을 간직해보세요"
                binding.tvAddImage.text = "직관후기 사진을\n올려주세요"
                binding.tvReviewMySeat.text = "내 직관 후기"
            }
            else -> {}
        }
    }

    private fun observeReviewViewModel() {
        viewModel.selectedImages.asLiveData().observe(this) { image ->
            updateNextButtonState()
        }

        viewModel.reviewCount.asLiveData().observe(this) { count ->
            binding.tvMySeatReviewCount.text = "${count}개"
            binding.layoutReviewNumber.visibility = if (count > 0) VISIBLE else GONE
        }

        viewModel.selectedGoodReview.asLiveData().observe(this) { count ->
            updateNextButtonState()
        }

        viewModel.selectedBadReview.asLiveData().observe(this) { count ->
            updateNextButtonState()
        }
    }

    private fun initSeatInfoView() {
        supportFragmentManager.setFragmentResultListener(FRAGMENT_RESULT_KEY, this) { _, bundle ->
            val newSelectedImages = bundle.getStringArrayList(SELECTED_IMAGES)
            newSelectedImages?.let { addSelectedImages(it) }
        }
        supportFragmentManager.setFragmentResultListener("selectSeatResult", this) { _, bundle ->
            binding.layoutSeatInfo.visibility = VISIBLE
            val seatZone = bundle.getString("seatZone", "")
            val block = bundle.getString("block", "")
            val column = bundle.getString("column", "")
            val number = bundle.getString("number", "")
            val sectionId = bundle.getInt("sectionId", 0)
            val isColumnCheckEnabled = bundle.getBoolean("isColumnCheckEnabled", false)
            with(binding) {
                when (sectionId) {
                    10 -> {
                        tvSeatColor.visibility = VISIBLE
                        tvSeatColor.text = seatZone
                        tvSeatBlock.text = viewModel.getBlockListName(block)
                        tvBlock.visibility = VISIBLE
                        tvColumnNumber.visibility = GONE
                        tvColumn.visibility = GONE
                        tvSeatNumber.visibility = VISIBLE
                        tvSeatNumber.text = "W$number"
                        tvNumber.visibility = VISIBLE
                    }

                    8 -> {
                        tvSeatColor.visibility = GONE
                        tvSeatBlock.text = viewModel.getBlockListName(block)
                        tvBlock.visibility = GONE
                        tvColumnNumber.visibility = VISIBLE
                        tvColumnNumber.text = column
                        tvColumn.visibility = VISIBLE
                        tvSeatNumber.text = number
                        tvSeatNumber.visibility = VISIBLE
                        tvNumber.visibility = VISIBLE
                    }

                    1 -> {
                        tvSeatColor.visibility = VISIBLE
                        tvSeatColor.text = seatZone
                        tvSeatBlock.text = viewModel.getBlockListName(block)
                        tvBlock.visibility = VISIBLE
                        tvColumnNumber.visibility = VISIBLE
                        tvColumnNumber.text = column
                        tvColumn.visibility = VISIBLE
                        tvSeatNumber.text = number
                        tvSeatNumber.visibility = VISIBLE
                        tvNumber.visibility = VISIBLE
                    }

                    else -> {
                        tvSeatColor.visibility = VISIBLE
                        tvSeatColor.text = seatZone
                        tvSeatBlock.text = block
                        tvBlock.visibility = VISIBLE
                        tvColumnNumber.visibility = VISIBLE
                        tvColumnNumber.text = column
                        tvColumn.visibility = VISIBLE
                        if (isColumnCheckEnabled) {
                            tvSeatNumber.visibility = GONE
                            tvNumber.visibility = GONE
                        } else {
                            tvSeatNumber.text = number
                            tvSeatNumber.visibility = VISIBLE
                            tvNumber.visibility = VISIBLE
                        }
                    }
                }
            }
        }
        updateNextButtonState()
    }

    private fun initDatePickerDialog() {
        binding.layoutDatePicker.setOnSingleClickListener {
            DatePickerDialog().show(supportFragmentManager, DATE_PICKER_DIALOG_TAG)
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
            ImageUploadDialog().show(supportFragmentManager, IMAGE_UPLOAD_DIALOG)
        }
    }

    private fun initEventToHome() {
        binding.btnBack.setOnSingleClickListener {
            Intent(this, HomeActivity::class.java).apply { startActivity(this) }
        }
    }

    private fun observeStadiumName() {
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
            if (supportFragmentManager.findFragmentByTag(REVIEW_MY_SEAT_DIALOG) == null &&
                supportFragmentManager.findFragmentByTag(SELECT_SEAT_DIALOG) == null
            ) {
                ReviewMySeatDialog().apply {
                    arguments = Bundle().apply { putString("METHOD_KEY", method?.name) }
                }.show(supportFragmentManager, REVIEW_MY_SEAT_DIALOG)
            }
        }

        binding.layoutSeatInfoNext.setOnSingleClickListener {
            if (supportFragmentManager.findFragmentByTag(SELECT_SEAT_DIALOG) == null &&
                supportFragmentManager.findFragmentByTag(REVIEW_MY_SEAT_DIALOG) == null
            ) {
                SelectSeatDialog().show(supportFragmentManager, SELECT_SEAT_DIALOG)
            }
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
        val isSelectedColumnFilled = viewModel.selectedColumn.value.isNotEmpty()
        val isSelectedNumberFilled = viewModel.selectedNumber.value.isNotEmpty()

        with(binding.tvUploadBtn) {
            val isReadyToUpload = isSelectedDateFilled && isSelectedImageFilled &&
                (isSelectedGoodBtnFilled || isSelectedBadBtnFilled) &&
                isSelectedBlockFilled &&
                (isSelectedColumnFilled || isSelectedNumberFilled)

            if (isReadyToUpload) {
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
            val isSelectedGoodBtnFilled = viewModel.selectedGoodReview.value.isNotEmpty()
            val isSelectedBadBtnFilled = viewModel.selectedBadReview.value.isNotEmpty()
            val isSelectedBlockFilled = viewModel.selectedBlock.value.isNotEmpty()
            val isSelectedColumnFilled = viewModel.selectedColumn.value.isNotEmpty()
            val isSelectedNumberFilled = viewModel.selectedNumber.value.isNotEmpty()
            when {
                !(isSelectedGoodBtnFilled || isSelectedBadBtnFilled) && (isSelectedBlockFilled || (isSelectedColumnFilled || isSelectedNumberFilled)) -> {
                    binding.tvUploadBtn.setBackgroundResource(R.drawable.rect_action_disabled_fill_8)
                    makeSpotImageAppbar("내 시야 후기를 등록해주세요")
                }
                !(isSelectedBlockFilled && (isSelectedColumnFilled || isSelectedNumberFilled)) && (isSelectedGoodBtnFilled || isSelectedBadBtnFilled) -> {
                    binding.tvUploadBtn.setBackgroundResource(R.drawable.rect_action_disabled_fill_8)
                    makeSpotImageAppbar("좌석을 선택해주세요")
                }
                ((!isSelectedGoodBtnFilled && !isSelectedBadBtnFilled) || !(isSelectedBlockFilled && (isSelectedColumnFilled || isSelectedNumberFilled))) -> {
                    binding.tvUploadBtn.setBackgroundResource(R.drawable.rect_action_disabled_fill_8)
                }
                else -> {
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
        }
    }

    private fun observePreSignedUrl() {
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

    private fun observeUploadImageToS3() {
        viewModel.count.asLiveData().observe(this) {
            if (it == selectedImageUris.size && it != 0) {
                when (method) {
                    ReviewMethod.VIEW -> {
                        viewModel.postSeatReview(ReviewMethod.VIEW)
                    }
                    ReviewMethod.FEED -> {
                        viewModel.postSeatReview(ReviewMethod.FEED)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun observeUploadReview() {
        viewModel.postReviewState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    val dialogType = when (method) {
                        ReviewMethod.VIEW -> ReviewMethod.VIEW
                        ReviewMethod.FEED -> ReviewMethod.FEED
                        else -> null
                    }
                    val reviewData = ReviewData(
                        selectedColumn = viewModel.selectedColumn.value,
                        selectedNumber = viewModel.selectedNumber.value,
                        preSignedUrlImages = viewModel.preSignedUrlImages.value,
                        selectedGoodReview = viewModel.selectedGoodReview.value,
                        selectedBadReview = viewModel.selectedBadReview.value,
                        detailReviewText = viewModel.detailReviewText.value,
                        selectedDate = viewModel.selectedDate.value,
                    )
                    dialogType?.let {
                        Intent(this, HomeActivity::class.java).apply {
                            putExtra("DIALOG_TYPE", dialogType)
                            putExtra("REVIEW_DATA", reviewData)
                            startActivity(this)
                        }
                    }
                }
                is UiState.Failure -> {
                    toast("리뷰 등록 실패: $state")
                }

                else -> {}
            }
        }
    }

    private fun makeSpotImageAppbar(message: String) {
        SpotImageSnackBar.make(
            view = binding.root,
            message = message,
            messageColor = com.depromeet.designsystem.R.color.color_foreground_white,
            icon = com.depromeet.designsystem.R.drawable.ic_alert_circle,
            iconColor = com.depromeet.designsystem.R.color.color_error_secondary,
            marginBottom = 96,
        ).show()
    }
}
