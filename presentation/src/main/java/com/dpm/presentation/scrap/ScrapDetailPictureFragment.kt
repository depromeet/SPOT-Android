package com.dpm.presentation.scrap

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import androidx.viewpager2.widget.ViewPager2
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentScrapDetailPictureBinding
import com.dpm.core.base.BindingFragment
import com.dpm.core.state.UiState
import com.dpm.presentation.scrap.adapter.ScrapDetailAdapter
import com.dpm.presentation.scrap.viewmodel.ScrapViewModel
import com.dpm.presentation.viewfinder.dialog.ReportDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScrapDetailPictureFragment : BindingFragment<FragmentScrapDetailPictureBinding>(
    R.layout.fragment_scrap_detail_picture, FragmentScrapDetailPictureBinding::inflate
) {
    companion object {
        const val TAG = "ScrapDetailPictureFragment"
    }

    private val viewModel: ScrapViewModel by activityViewModels()
    private lateinit var adapter: ScrapDetailAdapter
    private var isLoading: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        initObserve()
    }

    private fun initView() {
        initViewPager()
    }

    private fun initEvent() = with(binding) {
        onBackPressed()
        root.setOnClickListener {
            return@setOnClickListener
        }
        spotAppbar.setMenuOnClickListener {
            startToBottomSheetReportDialog(ReportDialog.newInstance())
        }
        spotAppbar.setNavigationOnClickListener {
            removeFragment()
        }
    }

    private fun initObserve() {
        viewModel.scrap.asLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    adapter.submitList(state.data.reviews)
                    binding.vpScrap.post {
                        binding.vpScrap.setCurrentItem(viewModel.currentPage.value, false)
                    }
                    isLoading = false
                }

                else -> {}
            }
        }
    }

    private fun initViewPager() {
        adapter = ScrapDetailAdapter(
            scrapClick = {
                viewModel.updateScrap(it)
            },
            likeClick = {
                viewModel.updateLike(it)
            },
            shareClick = {
                //TODO : 공유하기 클릭
            }
        )
        binding.vpScrap.adapter = adapter

        setupPageChangeListener()
    }

    private fun setupPageChangeListener() {
        binding.vpScrap.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    viewModel.setCurrentPage(binding.vpScrap.currentItem)
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.spotAppbar.setText((viewModel.scrap.value as UiState.Success).data.reviews[position].baseReview.formattedStadiumToSection())
                if (!isLoading && position >= adapter.itemCount - 2 && (viewModel.scrap.value as UiState.Success).data.hasNext) {
                    isLoading = true
                    viewModel.getNextScrapRecord()
                }
            }
        })
    }


    private fun startToBottomSheetReportDialog(dialogInstance: DialogFragment) {
        dialogInstance.show(parentFragmentManager, ReportDialog.TAG)
    }

    private fun removeFragment() {
        val fragment = parentFragmentManager.findFragmentByTag(TAG)
        if (fragment != null) {
            parentFragmentManager.commit {
                remove(fragment)
            }
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val fragment = parentFragmentManager.findFragmentByTag(TAG)
                    if (fragment != null) {
                        parentFragmentManager.beginTransaction()
                            .remove(fragment)
                            .commit()
                    }
                }
            })
    }

}