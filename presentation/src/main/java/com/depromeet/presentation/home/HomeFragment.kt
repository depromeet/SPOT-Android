package com.depromeet.presentation.home

import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(
    layoutResId = R.layout.fragment_home,
    bindingInflater = FragmentHomeBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivHomeProfile.setOnClickListener { navigateToProfileEditFragment() }
        binding.ibHomeEdit.setOnClickListener { navigateToProfileEditFragment() }
    }

    private fun fadeOutView(view: View) {
        view.animate()
            .alpha(0f)
            .setDuration(1000)
            .withEndAction { view.visibility = View.GONE }
    }


    private fun navigateToProfileEditFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fcv_home, ProfileEditFragment())
            .addToBackStack(null)
            .commit()
    }
}