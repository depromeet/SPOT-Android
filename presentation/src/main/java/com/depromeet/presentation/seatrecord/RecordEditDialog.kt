package com.depromeet.presentation.seatrecord

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentRecordEditBottomSheetBinding
import com.depromeet.presentation.seatrecord.viewmodel.SeatDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordEditDialog : BindingBottomSheetDialog<FragmentRecordEditBottomSheetBinding>(
    R.layout.fragment_record_edit_bottom_sheet,
    FragmentRecordEditBottomSheetBinding::inflate
) {
    private val viewModel: SeatDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateEditMethod()
    }

    private fun navigateEditMethod() {
        binding.tvRecordEdit.setOnClickListener {
            /** 수정 이동 추후 합칠 때 추가하기*/
            dismiss()
        }
        binding.tvRecordDelete.setOnClickListener {
            viewModel.setDeleteEvent()
            dismiss()
        }
    }
}