package com.depromeet.presentation.gallery

import android.os.Bundle
import androidx.compose.material.MaterialTheme
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityGalleryBinding

class GalleryActivity: BaseActivity<ActivityGalleryBinding>(
    ActivityGalleryBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.cvGallery.setContent {
            MaterialTheme {
                GalleryScreen()
            }
        }
    }
}