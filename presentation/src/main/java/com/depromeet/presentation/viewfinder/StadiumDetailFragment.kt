package com.depromeet.presentation.viewfinder

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentStadiumDetailBinding
import com.depromeet.presentation.viewfinder.compose.StadiumDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StadiumDetailFragment : BindingFragment<FragmentStadiumDetailBinding>(
    R.layout.fragment_stadium_detail, FragmentStadiumDetailBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val area = arguments?.getString(StadiumActivity.STADIUM_AREA)

        binding.spotAppbar.setNavigationOnClickListener {
            removeFragment()
        }

        binding.spotAppbar.setMenuOnClickListener {
            // go to home
        }

        binding.composeView.setContent {
            StadiumDetailScreen()
        }
    }

    private fun removeFragment() {
        val fragment = parentFragmentManager.findFragmentByTag(TAG)
        if (fragment != null) {
            parentFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
    }

    companion object {
        const val TAG = "StadiumDetailFragment"

        fun newInstance(): StadiumDetailFragment {
            val args = Bundle()

            val fragment = StadiumDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}