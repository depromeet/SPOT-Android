package com.depromeet.presentation.seatReview

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityReviewMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ReviewMainActivity : BaseActivity<ActivityReviewMainBinding>({
    ActivityReviewMainBinding.inflate(it)
}) {

    private val imageViews: List<View> by lazy { listOf(binding.ivFirstImage, binding.ivSecondImage, binding.ivThirdImage) }
    private var selectedImageUris: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUploadDialog()
        initDatePickerDialog()
        setupFragmentResultListener()
    }

    private fun initUploadDialog() {
        binding.btnAddPhoto.setOnClickListener {
            val uploadDialogFragment = UploadDialogFragment()
            uploadDialogFragment.show(supportFragmentManager, uploadDialogFragment.tag)
        }
    }

    private fun initDatePickerDialog() {
        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yy.MM.dd", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(today.time)
        binding.layoutDate.setOnClickListener {
            val datePickerDialogFragment = DatePickerDialogFragment().apply {
                onDateSelected = { year, month, day ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, day)
                    binding.tvDate.text = dateFormat.format(selectedDate.time)
                }
            }
            datePickerDialogFragment.show(supportFragmentManager, datePickerDialogFragment.tag)
        }
    }

    private fun setupFragmentResultListener() {
        supportFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val newSelectedImages = bundle.getStringArrayList("selected_images")
            newSelectedImages?.let { updateSelectedImages(it) }
        }
    }

    private fun updateSelectedImages(newImageUris: List<String>) {
        selectedImageUris.addAll(newImageUris)
        binding.tvAddPhoto.isVisible = selectedImageUris.isEmpty()

        for ((index, uri) in selectedImageUris.withIndex()) {
            if (index < imageViews.size) {
                val image = imageViews[index] as ImageView
                image.isVisible = true
                image.setImageURI(Uri.parse(uri))
            }
        }
        if (selectedImageUris.size == 3) {
            binding.svReviewPhoto.post {
                binding.svReviewPhoto.fullScroll(View.FOCUS_RIGHT)
            }
        }
        binding.btnAddPhoto.isVisible = selectedImageUris.size < imageViews.size
    }
}
