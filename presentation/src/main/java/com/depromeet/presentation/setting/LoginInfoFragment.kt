package com.depromeet.presentation.setting

import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentLoginInfoBinding

class LoginInfoFragment : BindingFragment<FragmentLoginInfoBinding>(
    layoutResId = R.layout.fragment_login_info,
    FragmentLoginInfoBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}