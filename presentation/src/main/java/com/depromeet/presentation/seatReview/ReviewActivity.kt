package com.depromeet.presentation.seatReview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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
import com.depromeet.presentation.seatReview.dialog.DatePickerDialog
import com.depromeet.presentation.seatReview.dialog.ImageUploadDialog
import com.depromeet.presentation.seatReview.dialog.ReviewMySeatDialog
import com.depromeet.presentation.seatReview.dialog.SelectSeatDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ReviewActivity : BaseActivity<ActivityReviewBinding>({
    ActivityReviewBinding.inflate(it)
}) {
    companion object {
        private const val DATE_FORMAT = "yy.MM.dd"
        private const val FRAGMENT_RESULT_KEY = "requestKey"
        private const val SELECTED_IMAGES = "selected_images"
        private const val MAX_SELECTED_IMAGES = 3
    }

    private val viewModel by viewModels<ReviewViewModel>()
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
        viewModel.getStadiumName()
        observeStadiumName()
        initDatePickerDialog()
        initUploadDialog()
        initSeatReviewDialog()
        setupFragmentResultListener()
        setupRemoveButtons()
        navigateToReviewDoneActivity()
    }

    private fun observeReviewViewModel() {
        viewModel.selectedDate.asLiveData().observe(this) { date ->
            binding.tvDate.text = date
            updateNextButtonState()
        }
        viewModel.selectedImages.asLiveData().observe(this) { image ->
            updateNextButtonState()
        }

        viewModel.reviewCount.asLiveData().observe(this) { count ->
            binding.tvMySeatReviewCount.text = count.toString()
            binding.layoutReviewNumber.visibility = if (count > 0) View.VISIBLE else View.GONE
        }

        viewModel.selectedGoodReview.asLiveData().observe(this) { count ->
            updateNextButtonState()
        }

        viewModel.selectedBadReview.asLiveData().observe(this) { count ->
            updateNextButtonState()
        }

        viewModel.selectedSeatZone.asLiveData().observe(this) { name ->
            binding.tvSeatColor.text = name.toString()
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
            binding.tvSeatNumber.text = number.toString()
            updateLayoutSeatInfoVisibility()
            updateNextButtonState()
        }
    }

    private fun observeStadiumName() {
        viewModel.stadiumNameState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    val firstStadium = state.data.firstOrNull()
                    if (firstStadium != null) {
                        binding.tvStadiumName.text = firstStadium.name
                        viewModel.getStadiumSection(firstStadium.id)
                        viewModel.setSelectedStadiumId(firstStadium.id)
                    }
                    observeReviewViewModel()
                }
                is UiState.Failure -> { toast("오류가 발생했습니다") }
                is UiState.Loading -> {}
                is UiState.Empty -> { toast("오류가 발생했습니다") }
                else -> {}
            }
        }
    }

    private fun initDatePickerDialog() {
        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        with(binding) {
            tvDate.text = dateFormat.format(today.time)
            layoutDatePicker.setOnSingleClickListener {
                val datePickerDialogFragment = DatePickerDialog()
                datePickerDialogFragment.show(supportFragmentManager, datePickerDialogFragment.tag)
            }
        }
    }

    private fun initUploadDialog() {
        binding.btnAddImage.setOnClickListener {
            val uploadDialogFragment = ImageUploadDialog()
            uploadDialogFragment.show(supportFragmentManager, uploadDialogFragment.tag)
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

    private fun setupRemoveButtons() {
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
        if (seatName.isNullOrEmpty() || block.isNullOrEmpty() || column.isNullOrEmpty() || number.isNullOrEmpty()) {
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
                val image = selectedImage[index]
                val layout = selectedImageLayout[index]
                layout.isVisible = false
                removeButtons[index].isVisible = false
            }
            if (selectedImageUris.size == MAX_SELECTED_IMAGES) {
                svAddImage.post { svAddImage.fullScroll(View.FOCUS_RIGHT) }
            }
            btnAddImage.isVisible = selectedImageUris.size < selectedImage.size
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
                setBackgroundResource(R.drawable.rect_gray900_fill_6)
                setTextColor(ContextCompat.getColor(this@ReviewActivity, android.R.color.white))
            } else {
                setBackgroundResource(R.drawable.rect_gray200_fill_6)
                setTextColor(ContextCompat.getColor(this@ReviewActivity, R.color.white))
            }
        }
    }

    private fun navigateToReviewDoneActivity() {
        binding.tvUploadBtn.setOnSingleClickListener {
            Intent(this, ReviewDoneActivity::class.java).apply { startActivity(this) }
        }
    }
}
