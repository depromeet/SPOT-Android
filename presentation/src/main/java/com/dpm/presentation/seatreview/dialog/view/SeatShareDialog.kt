package com.dpm.presentation.seatreview.dialog.view

import ReviewData
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSightShareBottomSheetBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.seatreview.SelectViewImageActivity

class SeatShareDialog : BindingBottomSheetDialog<FragmentSightShareBottomSheetBinding>(
    R.layout.fragment_sight_share_bottom_sheet,
    FragmentSightShareBottomSheetBinding::inflate,
) {

    companion object {
        private const val REVIEW_DATA = "REVIEW_DATA"
    }

    private val density by lazy { requireContext().resources.displayMetrics.density }
    private val radiusPx by lazy { 8 * density }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentMaterialDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initView()
        binding.csbvSelectImage.setTextPart("시야 사진이 ", "1장 이상", "인 경우 선택해주세요")
    }

    private fun initEvent() {
        val reviewData = arguments?.getParcelable<ReviewData>(REVIEW_DATA)
        binding.btnYes.setOnSingleClickListener {
            startActivity(Intent(requireContext(), SelectViewImageActivity::class.java))
            startActivity(
                Intent(requireContext(), SelectViewImageActivity::class.java).apply {
                    putExtra(REVIEW_DATA, reviewData)
                },
            )
        }
        binding.btnNo.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun initView() {
        getImagesUrl()
    }

    private fun getImagesUrl() {
        val reviewData = arguments?.getParcelable<ReviewData>(REVIEW_DATA)
        if (reviewData != null) {
            updateImageViews(reviewData.preSignedUrlImages)
        }
    }

    private fun updateImageViews(urls: List<String>) {
        val selectImages = listOf(
            binding.clFirstImage to binding.ivFirstImage,
            binding.clSecondImage to binding.ivSecondImage,
            binding.clThirdImage to binding.ivThirdImage,
        )

        selectImages.forEachIndexed { index, (view, image) ->
            if (index < urls.size) {
                view.visibility = VISIBLE
                val imageUri = urls[index]
                image.load(imageUri) {
                    transformations(RoundedCornersTransformation(radiusPx))
                }
            } else {
                view.visibility = GONE
            }
        }
    }
}
