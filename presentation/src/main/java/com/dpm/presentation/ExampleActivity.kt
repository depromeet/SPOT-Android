package com.dpm.presentation

import android.os.Bundle
import com.depromeet.presentation.databinding.ActivityExampleBinding
import com.dpm.core.base.BaseActivity
import com.dpm.presentation.extension.toast
import com.dpm.presentation.util.KakaoUtils
import com.dpm.presentation.util.mockDefaultFeed

class ExampleActivity : BaseActivity<ActivityExampleBinding>(ActivityExampleBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * @sample : 카카오 공유하기
         * @author : 조관희
         */
        binding.btnKakaoShare.setOnClickListener {
            KakaoUtils().share(
                this,
                template = mockDefaultFeed,
                onSuccess = { sharingIntent ->
                    startActivity(sharingIntent)
                },
                onFailure = { throwable ->
                    toast(throwable.message ?: "failed")
                }
            )
        }
    }
}