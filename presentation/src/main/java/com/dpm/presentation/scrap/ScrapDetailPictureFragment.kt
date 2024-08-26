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
import com.dpm.domain.entity.response.home.ResponseScrap
import com.dpm.presentation.scheme.SchemeKey
import com.dpm.presentation.scrap.adapter.ScrapDetailAdapter
import com.dpm.presentation.scrap.viewmodel.ScrapViewModel
import com.dpm.presentation.util.KakaoUtils
import com.dpm.presentation.util.Utils
import com.dpm.presentation.util.seatFeed
import com.dpm.presentation.viewfinder.dialog.ReportDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        initViewStatusBar()
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
            shareClick = { data, position ->
                shareLink(data, position)
            }
        )
        binding.vpScrap.adapter = adapter

        setupPageChangeListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Utils(requireContext()).apply {
            requireActivity().apply {
                setStatusBarColor(
                    window,
                    com.depromeet.designsystem.R.color.color_background_tertiary
                )
                setBlackSystemBarIconColor(window)
            }
        }
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

    private fun initViewStatusBar() {
        Utils(requireContext()).apply {
            requireActivity().apply {
                setStatusBarColor(window, com.depromeet.designsystem.R.color.color_b2000000)
            }
        }
    }

    private fun shareLink(data: ResponseScrap.ResponseReviewWrapper, imagePosition: Int) {
        KakaoUtils().share(
            requireContext(),
            seatFeed(
                title = data.baseReview.kakaoShareSeatFeedTitle(),
                description = "출처 : ${data.baseReview.member.nickname}",
                imageUrl = data.baseReview.images[imagePosition].url,
                queryParams = mapOf(
                    SchemeKey.STADIUM_ID to data.baseReview.stadium.id.toString(),
                    SchemeKey.BLOCK_CODE to data.baseReview.block.code
                )
            ),
            onSuccess = { sharingIntent ->
                requireContext().startActivity(sharingIntent)
            },
            onFailure = {
                Timber.d("링크 공유 실패 : ${it.message}")
            }
        )
    }
}