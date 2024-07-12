package com.depromeet.presentation.seatReview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityReviewBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import com.depromeet.presentation.seatReview.dialog.DatePickerDialog
import com.depromeet.presentation.seatReview.dialog.ImageUploadDialog
import com.depromeet.presentation.seatReview.dialog.ReviewMySeatDialog
import com.depromeet.presentation.seatReview.dialog.SelectSeatDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        listOf(binding.ivFirstImage, binding.ivSecondImage, binding.ivThirdImage)
    }
    private val selectedImageLayout: List<FrameLayout> by lazy {
        listOf(binding.layoutFirstImage, binding.layoutSecondImage, binding.layoutThirdImage)
    }

    private val removeButtons: List<ImageView> by lazy {
        listOf(binding.ivRemoveFirstImage, binding.ivRemoveSecondImage, binding.ivRemoveThirdImage)
    }

    private var selectedImageUris: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDatePickerDialog()
        initUploadDialog()
        initSeatReviewDialog()
        setupFragmentResultListener()
        observeUserDate()
        setupRemoveButtons()
        navigateToReviewDoneActivity()
    }

    private fun observeUserDate() {
        lifecycleScope.launch {
            viewModel.selectedDate.collect { date ->
                binding.tvDate.text = date
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
            val reviewMySeatDialogFragment = ReviewMySeatDialog()
            reviewMySeatDialogFragment.show(supportFragmentManager, reviewMySeatDialogFragment.tag)
        }
        binding.layoutSeatInfoNext.setOnSingleClickListener {
            val selectSeatDialogFragment = SelectSeatDialog()
            selectSeatDialogFragment.show(supportFragmentManager, selectSeatDialogFragment.tag)
        }
    }

    private fun initDatePickerDialog() {
        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        with(binding) {
            tvDate.text = dateFormat.format(today.time)
            layoutDatePicker.setOnSingleClickListener {
                val datePickerDialogFragment = DatePickerDialog().apply {
                    onDateSelected = { year, month, day ->
                        val selectedDate = Calendar.getInstance().apply {
                            set(year, month, day)
                        }
                        val formattedDate = dateFormat.format(selectedDate.time)
                        viewModel.updateSelectedDate(formattedDate)
                    }
                }
                datePickerDialogFragment.show(supportFragmentManager, datePickerDialogFragment.tag)
            }
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
        updateImageViews()
    }

    private fun updateImageViews() {
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
        }
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
            updateImageViews()
        }
    }

    private fun navigateToReviewDoneActivity() {
        binding.tvUploadBtn.setOnSingleClickListener {
            Intent(this, ReviewDoneActivity::class.java).apply { startActivity(this) }
        }
    }
}
