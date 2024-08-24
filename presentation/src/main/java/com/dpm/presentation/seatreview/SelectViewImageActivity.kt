package com.dpm.presentation.seatreview

import ReviewData
import android.os.Bundle
import androidx.activity.viewModels
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
    private lateinit var adapter: SelectKeywordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SelectKeywordAdapter()
        binding.rvKeywordList.adapter = adapter
        val mockData = listOf("Keyword 1", "Keyword 2", "Keyword 3", "Keyword 4", "Keyword 5")
        adapter.submitList(mockData)
        val reviewData = intent.getParcelableExtra<ReviewData>("REVIEW_DATA")
    }
}
