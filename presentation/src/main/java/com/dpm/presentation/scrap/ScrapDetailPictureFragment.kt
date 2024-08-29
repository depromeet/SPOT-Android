package com.dpm.presentation.scrap

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import androidx.viewpager2.widget.ViewPager2
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentScrapDetailPictureBinding
import com.dpm.core.base.BindingFragment
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotSnackBar
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
        initWindowInsets()
        viewModel.getDetailScrap()
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
        viewModel.detailScrap.asLiveData().observe(viewLifecycleOwner) { data ->
            adapter.submitList(data.map { it.baseReview }.toList())
            binding.vpScrap.setCurrentItem(viewModel.currentPage.value, false)
            isLoading = false
        }
    }

    private fun initViewPager() {
        adapter = ScrapDetailAdapter(
            scrapClick = { id, isScrap ->
                viewModel.updateScrap(id)
                if (isScrap) {
                    SpotSnackBar.make(
                        view = binding.root,
                        message = "스크랩이 해제되었어요!",
                        marginBottom = 20,
                        onClick = {},
                    ).show()
                } else {
                    SpotSnackBar.make(
                        view = binding.root,
                        message = "스크랩이 완료되었어요!",
                        endMessage = "스크랩으로 이동",
                        marginBottom = 20,
                    ) {
                        removeFragment()
                    }.show()
                }
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
        resetWindowInsets()
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
                binding.spotAppbar.setText(viewModel.detailScrap.value[position].baseReview.formattedStadiumToSection())
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
        viewModel.updateScrapRecord()
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
                    removeFragment()
                }
            })
    }

    private fun shareLink(data: ResponseScrap.ResponseBaseReview, imagePosition: Int) {
        KakaoUtils().share(
            requireContext(),
            seatFeed(
                title = data.kakaoShareSeatFeedTitle(),
                description = "출처 : ${data.member.nickname}",
                imageUrl = data.images[imagePosition].url,
                queryParams = mapOf(
                    SchemeKey.STADIUM_ID to data.stadium.id.toString(),
                    SchemeKey.BLOCK_CODE to data.block.code
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

    private fun initWindowInsets() {

        Utils(requireContext()).apply {
            requireActivity().apply {
                setStatusBarColor(window, R.color.transparent)
                setNavigationBarColor(window, com.depromeet.designsystem.R.color.black)
                setWhiteSystemBarIconColor(window)
                WindowCompat.setDecorFitsSystemWindows(window, false)
            }

            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    binding.spotAppbar.updatePadding(top = insets.top)
                    binding.vpScrap.updatePadding(bottom = insets.bottom)
                }
                WindowInsetsCompat.CONSUMED
            }
        }
    }

    private fun resetWindowInsets() {
        Utils(requireContext()).apply {
            requireActivity().apply {
                setStatusBarColor(
                    window,
                    com.depromeet.designsystem.R.color.color_background_tertiary
                )
                setNavigationBarColor(
                    window,
                    com.depromeet.designsystem.R.color.color_background_tertiary
                )
                setBlackSystemBarIconColor(window)
                WindowCompat.setDecorFitsSystemWindows(window, true)
            }
        }
    }

}