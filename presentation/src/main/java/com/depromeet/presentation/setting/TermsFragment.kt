package com.depromeet.presentation.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentTermsBinding

class TermsFragment : BindingFragment<FragmentTermsBinding>(
    layoutResId = R.layout.fragment_terms,
    FragmentTermsBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()
    }

    private fun initClickListener() {
        binding.tvTermsPersonalInfo.setOnClickListener {
            Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "https://wise-aphid-e39.notion.site/SPOT-f3b664cb537940e99617615f29e9b941?pvs=4"
                )
            ).apply {
                startActivity(this)
            }
        }

        binding.tvSettingTermsPolicy.setOnClickListener {
            Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "https://wise-aphid-e39.notion.site/SPOT-b63d909e51c548748bdbfd4363533b6a?pvs=4"
                )
            ).apply {
                startActivity(this)
            }
        }

        binding.appbarTerms.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}