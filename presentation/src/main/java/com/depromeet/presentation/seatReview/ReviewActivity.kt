package com.depromeet.presentation.seatReview

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityReviewBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ReviewActivity : BaseActivity<ActivityReviewBinding>({
    ActivityReviewBinding.inflate(it)
}) {

    private val viewModel by viewModels<ReviewViewModel>()

    private val imageViews: List<ImageView> by lazy {
        listOf(
            binding.ivFirstImage,
            binding.ivSecondImage,
            binding.ivThirdImage,
        )
    }
    private var selectedImageUris: MutableList<String> = mutableListOf()

    companion object {
        private const val DATE_FORMAT = "yy.MM.dd"
        private const val FRAGMENT_RESULT_KEY = "requestKey"
        private const val SELECTED_IMAGES = "selected_images"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDatePickerDialog()
        initUploadDialog()
        initSeatReviewDialog()
        setupFragmentResultListener()
        observeUserDate()
    }

    private fun observeUserDate() {
        viewModel.selectedDate.observe(this) { date ->
            binding.tvDate.text = date
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
                if (index < imageViews.size) {
                    val image = imageViews[index]
                    image.isVisible = true
                    image.setImageURI(Uri.parse(uri))
                    image.load(Uri.parse(uri)) {
                        transformations(RoundedCornersTransformation(26f))
                    }
                }
            }
            for (index in selectedImageUris.size until imageViews.size) {
                val image = imageViews[index]
                image.isVisible = false
            }
            if (selectedImageUris.size == 3) {
                svAddImage.post { svAddImage.fullScroll(View.FOCUS_RIGHT) }
            }
            btnAddImage.isVisible = selectedImageUris.size < imageViews.size
        }
    }
}
