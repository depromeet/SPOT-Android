package com.dpm.presentation.seatrecord

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentEditReviewBinding
import com.dpm.core.base.BindingFragment
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.presentation.extension.loadAndClip
import com.dpm.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.dpm.presentation.seatreview.dialog.main.DatePickerDialog
import com.dpm.presentation.util.CalendarUtil
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
        initMethodNaming(viewModel.currentReviewState.value)
    }

    private fun initEvent() {
        initDatePickerDialog()
    }

    private fun initObserver() {
        viewModel.editReview.asLiveData().observe(viewLifecycleOwner) {
            setDate(it.date)
            setImages(it.images.map { it.url })
            setSeatName(it.formattedSeatName())
            setCountReview(it)
        }

    }

    private fun initDatePickerDialog() {
        binding.layoutDatePicker.setOnClickListener {
            DatePickerDialog().show(parentFragmentManager, "DatePickerDialogTag")
        }
    }

    private fun initMethodNaming(reviewType: SeatRecordViewModel.ReviewType) {
        when (reviewType) {
            SeatRecordViewModel.ReviewType.SEAT_REVIEW -> {
                binding.tvTitle.text = "좌석의 시야를 공유해보세요"
                "야구장 시야 사진을\n올려주세요".also { binding.tvAddImage.text = it }
                binding.tvReviewMySeat.text = "내 시야 후기"
            }

            SeatRecordViewModel.ReviewType.INTUITIVE_REVIEW -> {
                binding.tvTitle.text = "경기의 순간을 간직해보세요"
                "직관후기 사진을\n올려주세요".also { binding.tvAddImage.text = it }
                binding.tvReviewMySeat.text = "내 직관 후기"
            }
        }
    }

    private fun setDate(date: String) {
        binding.tvDate.text = CalendarUtil.getFormattedDotDate(date)
    }

    private fun setImages(images: List<String>) {
        binding.tvImageCount.text = images.size.toString()
        if (images.isEmpty()) {
            binding.llAddImage.visibility = VISIBLE
            selectedImageLayout.forEach { it.visibility = GONE }
        } else {
            binding.llAddImage.visibility = GONE

            images.forEachIndexed { index, image ->
                selectedImage[index].loadAndClip(image)
                selectedImageLayout[index].visibility = VISIBLE
            }
            for (i in images.size until selectedImage.size) {
                selectedImageLayout[i].visibility = GONE
            }
        }

    }

    private fun setSeatName(seatName: String) {
        binding.tvSeatName.text = seatName
    }

    private fun setCountReview(data: ResponseMySeatRecord.ReviewResponse) {
        with(binding) {
            val keyWordSize = data.keywords.size
            layoutReviewNumber.visibility = when (keyWordSize) {
                0 -> GONE
                else -> VISIBLE
            }
            tvReviewCount.text = keyWordSize.toString()
        }
    }

}