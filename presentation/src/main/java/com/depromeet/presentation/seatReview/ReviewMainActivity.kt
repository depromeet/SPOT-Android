package com.depromeet.presentation.seatReview

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityMainReviewBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ReviewMainActivity : BaseActivity<ActivityMainReviewBinding>({
    ActivityMainReviewBinding.inflate(it)
}) {

    private val imageViews: List<View> by lazy {
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
        initUploadDialog()
        initDatePickerDialog()
        setupFragmentResultListener()
    }

    private fun initUploadDialog() {
        binding.btnAddImage.setOnClickListener {
            val uploadDialogFragment = ImageUploadDialog()
            uploadDialogFragment.show(supportFragmentManager, uploadDialogFragment.tag)
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
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(year, month, day)
                        tvDate.text = dateFormat.format(selectedDate.time)
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
                    val image = imageViews[index] as ImageView
                    image.isVisible = true
                    image.setImageURI(Uri.parse(uri))
                    image.load(Uri.parse(uri)) {
                        transformations(RoundedCornersTransformation(26f))
                    }
                }
            }
            for (index in selectedImageUris.size until imageViews.size) {
                val image = imageViews[index] as ImageView
                image.isVisible = false
            }
            if (selectedImageUris.size == 3) {
                svAddImage.post { svAddImage.fullScroll(View.FOCUS_RIGHT) }
            }
            btnAddImage.isVisible = selectedImageUris.size < imageViews.size
        }
    }
}
