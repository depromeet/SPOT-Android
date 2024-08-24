package com.dpm.presentation.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.compose.material.MaterialTheme
import com.depromeet.presentation.databinding.ActivityGalleryBinding
import com.dpm.core.base.BaseActivity
import com.dpm.presentation.seatreview.dialog.main.ImageUploadDialog.Companion.SELECTED_IMAGES
import com.dpm.presentation.util.ScreenType
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
        val screenType = intent.getStringExtra("screenType") ?: ScreenType.REVIEW.name
        binding.cvGallery.setContent {
            MaterialTheme {
                GalleryScreen(
                    screenType = screenType,
                    onImagesSelected = {
                        if (screenType == ScreenType.REVIEW.name) {
                            setResult(Activity.RESULT_OK, Intent().apply {
                                putStringArrayListExtra(SELECTED_IMAGES, ArrayList(it.map { uri -> uri.toString() }))
                            })
                            finish()
                        } else {

                        }
                    },
                    onBackPressed = { finish() }
                )
            }
        }
    }
}