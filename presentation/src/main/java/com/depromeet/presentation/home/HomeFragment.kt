package com.depromeet.presentation.home

import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(
    layoutResId = R.layout.fragment_home,
    bindingInflater = FragmentHomeBinding::inflate
) {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiScope.launch {
            delay(3000)
            fadeOutView(binding.csbvHomeHelp)
        }

        binding.csbvHomeHelp.setOnClickListener {
            fadeOutView(binding.csbvHomeHelp)
        }
        binding.ivHomeProfile.setOnClickListener { navigateToProfileEditFragment() }
        binding.ibHomeEdit.setOnClickListener { navigateToProfileEditFragment() }

    }

    private fun fadeOutView(view: View) {
        view.animate()
            .alpha(0f)
            .setDuration(1000)
            .withEndAction { view.visibility = View.GONE }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }


    private fun navigateToProfileEditFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fcv_home, ProfileEditFragment())
            .addToBackStack(null)
            .commit()
    }
}