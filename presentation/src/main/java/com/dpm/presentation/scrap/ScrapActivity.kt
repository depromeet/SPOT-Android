package com.dpm.presentation.scrap

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
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
import com.dpm.presentation.util.Utils
import com.dpm.presentation.viewfinder.StadiumSelectionActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ScrapActivity : BaseActivity<ActivityScrapBinding>(
    ActivityScrapBinding::inflate
) {
    private val viewModel: ScrapViewModel by viewModels()
    private lateinit var scrapAdapter: ScrapRecordAdapter
    private lateinit var scrapFilterAdapter: ScrapFilterAdapter
    private var isLoading = false


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
        initViewStatusBar()

    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadScrap()
    }

    private fun initEvent() = with(binding) {
        btScrapEmptyView.setOnClickListener {
            Intent(this@ScrapActivity, StadiumSelectionActivity::class.java).apply {
                startActivity(this)
            }
        }
        btScrapFailRefresh.setOnSingleClickListener {
            viewModel.getScrapRecord()
        }
        AppbarScrap.setNavigationOnClickListener {
            finish()
        }
        fabScrapUp.setOnClickListener {
            rvScrapRecord.smoothScrollToPosition(0)
        }
    }

    private fun initObserver() {
        viewModel.scrap.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    binding.tvScrapCount.text = state.data.totalScrapCount.toString()
                    scrapAdapter.submitList(state.data.reviews)
                    isLoading = false
                    setScrapScreenVisibility(ScrapScreenState.SUCCESS)
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
                viewModel.updateScrap(it.baseReview.id)
            },
            recordClick = {
                viewModel.setCurrentPage(it)
                startScrapDetailPictureFragment()
            }
        )
        binding.rvScrapRecord.adapter = scrapAdapter
        binding.rvScrapRecord.itemAnimator = null
        binding.rvScrapRecord.addItemDecoration(
            ScrapGridSpacingItemDecoration(
                spanCount = 2, spacing = 12.dpToPx(this), bottomSpacing = 40.dpToPx(this),
                borderMargin = 16.dpToPx(this)
            )
        )

        binding.rvScrapRecord.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val scrollBottom = !binding.rvScrapRecord.canScrollVertically(1)
                val scrollTop = !binding.rvScrapRecord.canScrollVertically(-1)

                if (scrollBottom && !isLoading && viewModel.scrap.value is UiState.Success && (viewModel.scrap.value as UiState.Success).data.hasNext) {
                    viewModel.getNextScrapRecord()
                    isLoading = true
                }
                binding.fabScrapUp.visibility = if (scrollTop) GONE else VISIBLE
            }
        })
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

    private fun initViewStatusBar() {
        Utils(this).apply {
            setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_tertiary)
            setBlackSystemBarIconColor(window)
        }
    }

    enum class ScrapScreenState {
        SUCCESS, EMPTY, FAIL
    }
}