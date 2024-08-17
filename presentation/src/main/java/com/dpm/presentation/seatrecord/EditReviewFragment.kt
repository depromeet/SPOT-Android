package com.dpm.presentation.seatrecord

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.dpm.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentEditReviewBinding
import com.dpm.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.dpm.presentation.seatreview.dialog.DatePickerDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditReviewFragment : BindingFragment<FragmentEditReviewBinding>(
    R.layout.fragment_edit_review,
    FragmentEditReviewBinding::inflate
) {
    companion object {
        private const val DATE_FORMAT = "yyyy.MM.dd"
        private const val ISO_DATE_FORMAT = "yyyy-MM-dd HH:mm"
        private const val FRAGMENT_RESULT_KEY = "requestKey"
        private const val SELECTED_IMAGES = "selected_images"
        private const val MAX_SELECTED_IMAGES = 3
        const val EDIT_REIVIEW_TAG = "editReview"
    }

    private val viewModel: SeatRecordViewModel by activityViewModels()
    private val selectedImage: List<ImageView> by lazy {
        listOf(
            binding.ivFirstImage,
            binding.ivSecondImage,
            binding.ivThirdImage
        )
    }
    private val selectedImageLayout: List<FrameLayout> by lazy {
        listOf(
            binding.layoutFirstImage,
            binding.layoutSecondImage,
            binding.layoutThirdImage
        )
    }
    private val removeButtons: List<ImageView> by lazy {
        listOf(
            binding.ivRemoveFirstImage,
            binding.ivRemoveSecondImage,
            binding.ivRemoveThirdImage
        )
    }
    private val selectedImageUris: MutableList<String> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {

    }

    private fun initEvent() {
        initDatePickerDialog()
    }

    private fun initObserver() {
        viewModel.editReview.asLiveData().observe(viewLifecycleOwner) {
            initDatePickerDialog()
        }
    }

    private fun initDatePickerDialog() {
        binding.layoutDatePicker.setOnClickListener {
            DatePickerDialog().show(parentFragmentManager, "DatePickerDialogTag")
        }
    }


}