package com.dpm.presentation.scrap

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityScrapBinding
import com.dpm.core.base.BaseActivity
import com.dpm.core.state.UiState
import com.dpm.designsystem.extension.dpToPx
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.scrap.adapter.ScrapFilterAdapter
import com.dpm.presentation.scrap.adapter.ScrapGridSpacingItemDecoration
import com.dpm.presentation.scrap.adapter.ScrapRecordAdapter
import com.dpm.presentation.scrap.dialog.ScrapFilterDialog
import com.dpm.presentation.scrap.viewmodel.ScrapViewModel
import com.dpm.presentation.viewfinder.StadiumActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ScrapActivity : BaseActivity<ActivityScrapBinding>(
    ActivityScrapBinding::inflate
) {
    private val viewModel: ScrapViewModel by viewModels()
    private lateinit var scrapAdapter: ScrapRecordAdapter
    private lateinit var scrapFilterAdapter: ScrapFilterAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        viewModel.getScrapRecord()
        initScrapAdapter()
        initScrapFilterAdapter()

    }

    private fun initEvent() = with(binding) {
        btScrapEmptyView.setOnClickListener {
            Intent(this@ScrapActivity, StadiumActivity::class.java).apply {
                startActivity(this)
            }
        }
        btScrapFailRefresh.setOnSingleClickListener {
            viewModel.getScrapRecord()
        }
        AppbarScrap.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initObserver() {
        viewModel.scrap.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    scrapAdapter.submitList(state.data.reviews)
                }

                is UiState.Failure -> {
                    setScrapScreenVisibility(ScrapScreenState.FAIL)
                }

                is UiState.Loading -> {

                }

                is UiState.Empty -> {
                    setScrapScreenVisibility(ScrapScreenState.EMPTY)
                }
            }
        }

        viewModel.filter.asLiveData().observe(this) {
            Timber.d("FILTER_TEST -> $it")
            scrapFilterAdapter.submitList(it) {
                scrapFilterAdapter.updateItem()
                binding.rvScrapFilter.scrollToPosition(0)
            }
        }
    }

    private fun initScrapAdapter() {
        scrapAdapter = ScrapRecordAdapter(
            scrapClick = {
                viewModel.deleteScrapRecord(it.baseReview.id)
            },
            recordClick = {
                startScrapDetailPictureFragment()
                supportFragmentManager.commit {
                    replace(
                        R.id.fcvScrap,
                        ScrapDetailPictureFragment(),
                        ScrapDetailPictureFragment.TAG
                    )
                }
            }
        )
        binding.rvScrapRecord.adapter = scrapAdapter
        binding.rvScrapRecord.addItemDecoration(
            ScrapGridSpacingItemDecoration(
                spanCount = 2, spacing = 12.dpToPx(this), bottomSpacing = 40.dpToPx(this)
            )
        )

    }

    private fun initScrapFilterAdapter() {
        scrapFilterAdapter = ScrapFilterAdapter(
            filterClick = {
                ScrapFilterDialog().show(supportFragmentManager, ScrapFilterDialog.TAG)
            },
            selectedClick = {
                viewModel.deleteFilter(it)
            }
        )
        binding.rvScrapFilter.adapter = scrapFilterAdapter
        binding.rvScrapFilter.itemAnimator = null
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

    private fun startScrapDetailPictureFragment() {
        supportFragmentManager.commit {
            replace(
                R.id.fcvScrap,
                ScrapDetailPictureFragment(),
                ScrapDetailPictureFragment.TAG
            )
        }
    }

    enum class ScrapScreenState {
        SUCCESS, EMPTY, FAIL
    }
}