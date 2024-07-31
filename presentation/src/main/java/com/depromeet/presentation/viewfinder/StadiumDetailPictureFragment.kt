package com.depromeet.presentation.viewfinder

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentStadiumDetailPictureBinding
import com.depromeet.presentation.viewfinder.compose.detailpicture.StadiumDetailPictureScreen
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    private fun initView() {
        getReviewExtra { reviewId, reviewIndex ->
            binding.cvReviewContent.setContent {
                StadiumDetailPictureScreen(
                    reviewId = reviewId,
                    reviewIndex = reviewIndex,
                    stadiumDetailViewModel = stadiumDetailViewModel,
                )
            }
        }
    }

    private fun initEvent() {
        onBackPressed()
        binding.spotAppbar.setNavigationOnClickListener {
            removeFragment()
        }
    }

    private fun getReviewExtra(callback: (id: Long, index: Int) -> Unit) {
        val reviewId = arguments?.getLong(StadiumDetailActivity.REVIEW_ID) ?: return
        val reviewIndex = arguments?.getInt(StadiumDetailActivity.REVIEW_INDEX) ?: return
        callback(reviewId, reviewIndex)
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