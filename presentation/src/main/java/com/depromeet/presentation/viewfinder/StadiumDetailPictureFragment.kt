package com.depromeet.presentation.viewfinder

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentStadiumDetailPictureBinding
import com.depromeet.presentation.extension.dpToPx
import com.depromeet.presentation.util.Utils
import com.depromeet.presentation.viewfinder.compose.detailpicture.StadiumDetailPictureScreen
import com.depromeet.presentation.viewfinder.dialog.ReportDialog
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

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

    private val stadiumDetailViewModel: StadiumDetailViewModel by activityViewModels()
    private val utils by lazy {
        Utils(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    private fun initView() {
        initWindowInsets()
        getReviewExtra { reviewId, reviewIndex, title ->
            binding.spotAppbar.setText(title)
            binding.cvReviewContent.setContent {
                MaterialTheme {
                    StadiumDetailPictureScreen(
                        reviewId = reviewId,
                        reviewIndex = reviewIndex,
                        stadiumDetailViewModel = stadiumDetailViewModel,
                    )
                }
            }
        }
    }

    private fun initEvent() {
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

    private fun getReviewExtra(callback: (id: Long, index: Int, title: String) -> Unit) {
        val reviewId = arguments?.getLong(StadiumDetailActivity.REVIEW_ID) ?: return
        val reviewIndex = arguments?.getInt(StadiumDetailActivity.REVIEW_INDEX) ?: return
        val title = arguments?.getString(StadiumDetailActivity.REVIEW_TITLE_WITH_STADIUM) ?: return
        callback(reviewId, reviewIndex, title)
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

    override fun onDestroy() {
        resetWindowInsets()
        super.onDestroy()
    }

    private fun resetWindowInsets() {
        utils.apply {
            requireActivity().apply {
                setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_white)
                setNavigationBarColor(window, com.depromeet.designsystem.R.color.color_background_white)
                setBlackSystemBarIconColor(window)
                WindowCompat.setDecorFitsSystemWindows(window, true)
            }
        }
    }
}
