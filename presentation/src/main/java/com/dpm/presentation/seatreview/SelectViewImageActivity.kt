package com.dpm.presentation.seatreview

import ReviewData
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.presentation.databinding.ActivitySelectViewImageBinding
import com.dpm.core.base.BaseActivity
import com.dpm.presentation.seatreview.adapter.SelectKeywordAdapter
import com.dpm.presentation.seatreview.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectViewImageActivity : BaseActivity<ActivitySelectViewImageBinding>({
    ActivitySelectViewImageBinding.inflate(it)
}) {
    private val viewModel by viewModels<ReviewViewModel>()
    private val density by lazy { resources.displayMetrics.density }
    private val radiusPx by lazy { 8 * density }
    private var _adapter: SelectKeywordAdapter? = null
    private val adapter get() = requireNotNull(_adapter) { "Adapter is not initialized" }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        _adapter = SelectKeywordAdapter()
        binding.rvKeywordList.adapter = adapter
        val reviewData = intent.getParcelableExtra<ReviewData>("REVIEW_DATA")
        reviewData?.let {
            val totalReview = it.selectedGoodReview + it.selectedBadReview
            adapter.submitList(totalReview)
            updateImageViews(it.preSignedUrlImages)
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
