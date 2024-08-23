package com.dpm.presentation.seatreview.dialog.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSightShareBottomSheetBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.seatreview.SelectViewImageActivity
import com.dpm.presentation.seatreview.viewmodel.ReviewViewModel

class SeatShareDialog : BindingBottomSheetDialog<FragmentSightShareBottomSheetBinding>(
    R.layout.fragment_sight_share_bottom_sheet,
    FragmentSightShareBottomSheetBinding::inflate,
) {
    private val viewModel: ReviewViewModel by activityViewModels()
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
        binding.csbvSelectImage.setText("시야 사진이 1장 이상인 경우 선택해주세요")
    }

    private fun initEvent() {
        binding.btnYes.setOnSingleClickListener {
            startActivity(Intent(requireContext(), SelectViewImageActivity::class.java))
        }
        binding.btnNo.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun initView() {
        getImagesUrl()
    }

    private fun getImagesUrl() {
        val selectedImageUris = arguments?.getStringArrayList("SELECTED_IMAGES") ?: arrayListOf()
        updateImageViews(selectedImageUris)
    }

    private fun updateImageViews(urls: List<String>) {
        val selectImages = listOf(
            binding.clFirstImage to binding.ivFirstImage,
            binding.clSecondImage to binding.ivSecondImage,
            binding.clThirdImage to binding.ivThirdImage,
        )

        selectImages.forEachIndexed { index, (cardView, imageView) ->
            if (index < urls.size) {
                cardView.visibility = VISIBLE
                val imageUri = urls[index]
                imageView.load(imageUri) {
                    transformations(RoundedCornersTransformation(radiusPx))
                }
            } else {
                cardView.visibility = GONE
            }
        }
    }
}
