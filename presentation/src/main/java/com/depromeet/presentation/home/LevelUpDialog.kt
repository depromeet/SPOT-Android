package com.depromeet.presentation.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.depromeet.core.base.BindingDialogFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentLevelupDialogBinding
import com.depromeet.presentation.extension.loadAndClip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LevelupDialog(
    private val title: String,
    private val level: Int,
    private val mascotImage: String,
) : BindingDialogFragment<FragmentLevelupDialogBinding>(
    layoutResId = R.layout.fragment_upload_error_dialog,
    bindingInflater = FragmentLevelupDialogBinding::inflate,
) {
    companion object {
        const val TAG = "LevelupDialog"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val params = attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params
        }
        with(binding) {
            when (level) {
                1, 4 -> {
                    tvLevelupDescription.text = "두근두근 ${title}로 레벨업했어요!"
                }

                2, 5, 6 -> {
                    tvLevelupDescription.text = "두근두근 ${title}로 레벨업했어요!"
                }

                3 -> {
                    tvLevelupDescription.text = "두근두근 ${title}를 갖게 되었어요!"
                }
            }
            ivLevelupMascotImage.loadAndClip(mascotImage)
            btErrorCheck.setOnClickListener {
                dismiss()
            }
        }
    }
}