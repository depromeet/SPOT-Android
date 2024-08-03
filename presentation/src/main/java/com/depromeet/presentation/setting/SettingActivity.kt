package com.depromeet.presentation.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivitySettingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(
    ActivitySettingBinding::inflate
){
    private val viewModel : SettingViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initEvent()


    }

    private fun initEvent() = with(binding){
        binding.appbarSetting.setNavigationOnClickListener {
            finish()
        }


    }
}