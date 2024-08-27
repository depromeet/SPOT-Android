package com.dpm.presentation.viewfinder

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.material.MaterialTheme
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentStadiumDetailPictureBinding
import com.dpm.core.base.BindingFragment
import com.dpm.designsystem.SpotSnackBar
import com.dpm.domain.preference.SharedPreference
import com.dpm.presentation.util.Utils
import com.dpm.presentation.viewfinder.compose.detailpicture.StadiumDetailPictureScreen
import com.dpm.presentation.viewfinder.dialog.ReportDialog
import com.dpm.presentation.viewfinder.viewmodel.StadiumDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

enum class DetailReviewEntryPoint {
    TOP_REVIEW, MAIN_REVIEW
}

@AndroidEntryPoint
class StadiumDetailPictureFragment : BindingFragment<FragmentStadiumDetailPictureBinding>(
    R.layout.fragment_stadium_detail_picture, FragmentStadiumDetailPictureBinding::inflate
) {
    companion object {
        const val TAG = "StadiumDetailPictureFragment"

        fun newInstance(): StadiumDetailPictureFragment {
            val args = Bundle()

            val fragment = StadiumDetailPictureFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var sharedPreference: SharedPreference

    private val stadiumDetailViewModel: StadiumDetailViewModel by activityViewModels()
    private val utils by lazy {
        Utils(requireActivity())
    }
    private lateinit var snackBar: SpotSnackBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    private fun initView() {
        initWindowInsets()
        initSnackBar()
        getReviewExtra { reviewId, reviewIndex, title, type ->
            binding.spotAppbar.setText(title)
            binding.cvReviewContent.setContent {
                MaterialTheme {
                    StadiumDetailPictureScreen(
                        reviewId = reviewId,
                        reviewIndex = reviewIndex,
                        type = type,
                        isFirstLike = sharedPreference.isFirstLike,
                        stadiumDetailViewModel = stadiumDetailViewModel,
                        onClickLike = {
                            sharedPreference.isFirstLike = false
                        },
                        onClickScrap = { isScrap ->
                            if (isScrap) {
                                snackBar.show()
                            }
                        },
                        onClickShare = {
                            sharedPreference.isFirstShare = false
                        }
                    )
                }
            }
        }
    }

    private fun initEvent() {
        binding.root.setOnClickListener {
            return@setOnClickListener
        }
        onBackPressed()
        setOnClickSpotAppbar()
    }

    private fun initWindowInsets() {

        utils.apply {
            requireActivity().apply {
                setStatusBarColor(window, R.color.transparent)
                setNavigationBarColor(window, R.color.transparent)
                setWhiteSystemBarIconColor(window)
                WindowCompat.setDecorFitsSystemWindows(window, false)
            }

            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

                stadiumDetailViewModel.updateBottomPadding(insets.bottom / (resources.displayMetrics.density))

                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    binding.spotAppbar.updatePadding(top = insets.top)
                }
                WindowInsetsCompat.CONSUMED
            }
        }
    }

    private fun initSnackBar() {
        snackBar = SpotSnackBar.make(
            view = binding.root.rootView,
            background = com.depromeet.designsystem.R.drawable.rect_body_subtitle_fill_60,
            message = getString(R.string.viewfinder_snackbar_scrap),
            endMessage = getString(R.string.viewfinder_underscore_snackbar_scrap),
            onClick = {
                // TODO : 스크랩 화면으로 이동
            })
    }

    private fun getReviewExtra(callback: (id: Long, index: Int, title: String, type: String) -> Unit) {
        val reviewId = arguments?.getLong(StadiumDetailActivity.REVIEW_ID) ?: return
        val reviewIndex = arguments?.getInt(StadiumDetailActivity.REVIEW_INDEX) ?: return
        val title = arguments?.getString(StadiumDetailActivity.REVIEW_TITLE_WITH_STADIUM) ?: return
        val type = arguments?.getString(StadiumDetailActivity.REVIEW_TYPE) ?: return
        callback(reviewId, reviewIndex, title, type)
    }

    private fun removeFragment() {
        val fragment = parentFragmentManager.findFragmentByTag(TAG)
        if (fragment != null) {
            parentFragmentManager.commit {
                remove(fragment)
            }
        }
    }

    private fun startToBottomSheetReportDialog(dialogInstance: DialogFragment, tag: String) {
        dialogInstance.show(parentFragmentManager, tag)
    }

    private fun setOnClickSpotAppbar() {
        binding.spotAppbar.setNavigationOnClickListener {
            removeFragment()
        }
        binding.spotAppbar.setMenuOnClickListener {
            startToBottomSheetReportDialog(ReportDialog.newInstance(), ReportDialog.TAG)
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

    override fun onDestroyView() {
        snackBar.dismiss()
        resetWindowInsets()
        super.onDestroyView()
    }

    private fun resetWindowInsets() {
        utils.apply {
            requireActivity().apply {
                setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_white)
                setNavigationBarColor(
                    window,
                    com.depromeet.designsystem.R.color.color_background_white
                )
                setBlackSystemBarIconColor(window)
                WindowCompat.setDecorFitsSystemWindows(window, true)
            }
        }
    }
}
