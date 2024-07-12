package com.depromeet.presentation.viewfinder

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentStadiumDetailPictureBinding
import com.depromeet.presentation.extension.getCompatibleParcelableExtra
import com.depromeet.presentation.viewfinder.compose.detailpicture.StadiumDetailPictureScreen
import com.depromeet.presentation.viewfinder.sample.ReviewContent
import com.depromeet.presentation.viewfinder.sample.reviewContents

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val area =
            arguments?.getCompatibleParcelableExtra<ReviewContent>(StadiumDetailActivity.REVIEW_PICTURE_CONTENT)

        binding.spotAppbar.setNavigationOnClickListener {
            removeFragment()
        }

        binding.cvReviewContent.setContent {
            StadiumDetailPictureScreen(
                reviews = reviewContents,
            )
        }
    }

    private fun removeFragment() {
        val fragment = parentFragmentManager.findFragmentByTag(TAG)
        if (fragment != null) {
            parentFragmentManager.commit {
                remove(fragment)
            }
        }
    }
}