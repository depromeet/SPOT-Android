package com.dpm.presentation.gallery

import android.os.Bundle
import androidx.compose.material.MaterialTheme
import com.depromeet.presentation.databinding.ActivityGalleryBinding
import com.dpm.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity : BaseActivity<ActivityGalleryBinding>(
    ActivityGalleryBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.cvGallery.setContent {
            MaterialTheme {
                GalleryScreen {

                }
            }
        }
    }
}