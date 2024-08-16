package com.depromeet.presentation

import android.os.Bundle
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityExampleBinding
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.util.KakaoUtils
import com.depromeet.presentation.util.mockDefaultFeed

class ExampleActivity : BaseActivity<ActivityExampleBinding>(ActivityExampleBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * @sample : 카카오 공유하기
         * @author : 조관희
         */
        binding.btnKakaShare.setOnClickListener {
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