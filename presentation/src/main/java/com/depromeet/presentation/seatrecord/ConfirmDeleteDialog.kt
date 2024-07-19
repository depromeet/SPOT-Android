package com.depromeet.presentation.seatrecord

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingDialogFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentConfirmDeleteDialogBinding
import com.depromeet.presentation.seatrecord.viewmodel.SeatDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmDeleteDialog : BindingDialogFragment<FragmentConfirmDeleteDialogBinding>(
    R.layout.fragment_confirm_delete_dialog,
    FragmentConfirmDeleteDialogBinding::inflate
) {
    private val viewModel: SeatDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentDialogFragment)
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

        binding.btConfirmCheck.setOnClickListener {
            viewModel.removeReviewData()
            dismiss()
        }
        binding.btConfirmCancel.setOnClickListener {
            dismiss()
        }

    }


}