package com.depromeet.presentation.seatReview

import android.os.Bundle
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityReviewMainBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ReviewMainActivity : BaseActivity<ActivityReviewMainBinding>({
    ActivityReviewMainBinding.inflate(it)
}) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUploadDialog()
        initDatePickerDialog()
    }

    private fun initUploadDialog() {
        binding.btnAddPhoto.setOnSingleClickListener {
            val uploadDialogFragment = UploadDialogFragment()
            uploadDialogFragment.show(supportFragmentManager, uploadDialogFragment.tag)
        }
    }

    private fun initDatePickerDialog() {
        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yy.MM.dd", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(today.time)

        binding.layoutDate.setOnSingleClickListener {
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
}
