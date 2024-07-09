package com.depromeet.presentation.home

import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentHomeBinding

class HomeFragment : BindingFragment<FragmentHomeBinding>(
    layoutResId = R.layout.fragment_home,
    bindingInflater = FragmentHomeBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}