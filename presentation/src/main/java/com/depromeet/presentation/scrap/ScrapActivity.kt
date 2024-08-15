package com.depromeet.presentation.scrap

import android.os.Bundle
import androidx.activity.viewModels
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityScrapBinding
import com.depromeet.presentation.scrap.viewmodel.ScrapViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScrapActivity : BaseActivity<ActivityScrapBinding>(
    ActivityScrapBinding::inflate
) {
    private val viewModel : ScrapViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        initObserver()
    }

    private fun initView(){

    }

    private fun initEvent() {

    }

    private fun initObserver() {

    }
}