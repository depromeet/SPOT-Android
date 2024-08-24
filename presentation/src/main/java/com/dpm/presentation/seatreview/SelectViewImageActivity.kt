package com.dpm.presentation.seatreview

import ReviewData
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySelectViewImageBinding
import com.dpm.core.base.BaseActivity
import com.dpm.domain.model.seatreview.ReviewMethod
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.home.HomeActivity
import com.dpm.presentation.seatreview.adapter.SelectKeywordAdapter
import com.dpm.presentation.seatreview.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectViewImageActivity : BaseActivity<ActivitySelectViewImageBinding>({
    ActivitySelectViewImageBinding.inflate(it)
}) {

    companion object {
        private const val REVIEW_DATA = "REVIEW_DATA"
        private const val CANCEL_SNACKBAR = "CANCEL_SNACKBAR"
        private const val UPLOAD_SNACKBAR = "UPLOAD_SNACKBAR"
    }
    private val viewModel by viewModels<ReviewViewModel>()
    private val density by lazy { resources.displayMetrics.density }
    private val radiusPx by lazy { 8 * density }
    private var _adapter: SelectKeywordAdapter? = null
    private val adapter get() = requireNotNull(_adapter) { "Adapter is not initialized" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        observeSelectedImageUris()
    }

    private fun initView() {
        _adapter = SelectKeywordAdapter()
        binding.rvKeywordList.adapter = adapter
        val reviewData = intent.getParcelableExtra<ReviewData>(REVIEW_DATA)
        reviewData?.let {
            val totalReview = it.selectedGoodReview + it.selectedBadReview
            adapter.submitList(totalReview)
            updateImageViews(it.preSignedUrlImages)
        }
        setupImageSelection()
        initEvent()
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
                image.tag = imageUri
            } else {
                view.visibility = GONE
            }
        }
    }

    private fun setupImageSelection() {
        val selectImage = listOf(
            binding.ivFirstImage to binding.ivFirstImageCheck,
            binding.ivSecondImage to binding.ivSecondImageCheck,
            binding.ivThirdImage to binding.ivThirdImageCheck,
        )

        selectImage.forEach { (image, isChecked) ->
            image.setOnSingleClickListener {
                val imageUri = image.tag as? String
                if (imageUri != null) {
                    viewModel.toggleImageSelection(imageUri)
                }
            }
        }
    }

    private fun observeSelectedImageUris() {
        viewModel.selectViewImageUrl.asLiveData().observe(this) { selectedUris ->
            updateCheckImages(selectedUris)
            uploadBtnState(selectedUris.isNotEmpty())
        }
    }

    private fun updateCheckImages(selectedUris: List<String>) {
        val selectImage = listOf(
            binding.ivFirstImage to binding.ivFirstImageCheck,
            binding.ivSecondImage to binding.ivSecondImageCheck,
            binding.ivThirdImage to binding.ivThirdImageCheck,
        )

        selectImage.forEach { (image, isChecked) ->
            val imageUri = image.tag as? String
            if (imageUri != null) {
                val isSelected = selectedUris.contains(imageUri)
                val drawableResId = if (isSelected) R.drawable.ic_check_green else R.drawable.ic_uncheck
                isChecked.setImageResource(drawableResId)
                if (isSelected) {
                    image.setColorFilter(Color.argb(77, 0, 0, 0))
                } else {
                    image.clearColorFilter()
                }
            }
        }
    }

    private fun uploadBtnState(isEnabled: Boolean) {
        val backgroundResId = if (isEnabled) {
            R.drawable.rect_action_enabled_fill_8
        } else {
            R.drawable.rect_action_disabled_fill_8
        }
        binding.tvUploadBtn.setBackgroundResource(backgroundResId)
        binding.tvUploadBtn.isEnabled = isEnabled
    }

    private fun initEvent() {
        binding.tvCancel.setOnSingleClickListener {
            startActivity(
                Intent(this, HomeActivity::class.java).putExtra(
                    CANCEL_SNACKBAR,
                    true,
                ),
            ).also { finish() }
        }
        binding.tvUploadBtn.setOnSingleClickListener {
            val reviewData = intent.getParcelableExtra<ReviewData>(REVIEW_DATA)
            if (reviewData != null) {
                viewModel.updateViewReview(
                    reviewData.selectedColumn,
                    reviewData.selectedNumber,
                    viewModel.selectViewImageUrl.value.toList(),
                    reviewData.selectedGoodReview,
                    reviewData.selectedBadReview,
                    reviewData.detailReviewText,
                    reviewData.selectedDate,
                    reviewData.blockId,
                )
            }
            viewModel.postSeatReview(ReviewMethod.VIEW)
            startActivity(
                Intent(this, HomeActivity::class.java).putExtra(
                    UPLOAD_SNACKBAR,
                    true,
                ),
            ); finish()
        }
        binding.ivExit.setOnSingleClickListener {
            finish()
        }
    }
}
