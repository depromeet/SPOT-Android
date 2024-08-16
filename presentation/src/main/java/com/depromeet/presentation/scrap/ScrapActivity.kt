package com.depromeet.presentation.scrap

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.presentation.databinding.ActivityScrapBinding
import com.depromeet.presentation.extension.dpToPx
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.scrap.adapter.ScrapGridSpacingItemDecoration
import com.depromeet.presentation.scrap.adapter.ScrapRecordAdapter
import com.depromeet.presentation.scrap.viewmodel.ScrapViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScrapActivity : BaseActivity<ActivityScrapBinding>(
    ActivityScrapBinding::inflate
) {
    private val viewModel: ScrapViewModel by viewModels()
    private lateinit var scrapAdapter: ScrapRecordAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        viewModel.getScrapRecord()
        initScrapAdapter()
    }

    private fun initEvent() {

    }

    private fun initObserver() {
        viewModel.scrap.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    scrapAdapter.submitList(state.data)
                }

                is UiState.Failure -> {
                    setScrapScreenVisibility(ScrapScreenState.FAIL)
                }

                is UiState.Loading -> {
                    //TODO : shimmer skeleton 추가하기
                }

                is UiState.Empty -> {
                    setScrapScreenVisibility(ScrapScreenState.EMPTY)
                }
            }
        }
    }

    private fun initScrapAdapter() {
        scrapAdapter = ScrapRecordAdapter(
            scrapClick = {
                toast("스크랩 클릭 ${it.id}")
                //TODO : 스크랩 삭제
            },
            recordClick = {
                toast("게시물 클릭 ${it.id}")
                //TODO : 게시물 상세화면 이동
            }
        )
        binding.rvScrapRecord.adapter = scrapAdapter
        binding.rvScrapRecord.addItemDecoration(
            ScrapGridSpacingItemDecoration(
                spanCount = 2, spacing = 12.dpToPx(this), bottomSpacing = 40.dpToPx(this)
            )
        )

    }

    private fun setScrapScreenVisibility(scrapState: ScrapScreenState) = with(binding) {
        when (scrapState) {
            ScrapScreenState.SUCCESS -> {
                rvScrapRecord.visibility = VISIBLE
                clScrapEmpty.visibility = GONE
                clScrapFail.visibility = GONE
            }

            ScrapScreenState.EMPTY -> {
                rvScrapRecord.visibility = GONE
                clScrapEmpty.visibility = VISIBLE
                clScrapFail.visibility = GONE
            }

            ScrapScreenState.FAIL -> {
                rvScrapRecord.visibility = GONE
                clScrapEmpty.visibility = GONE
                clScrapFail.visibility = VISIBLE
            }
        }
    }

    enum class ScrapScreenState {
        SUCCESS, EMPTY, FAIL
    }
}