package com.depromeet.presentation.seatrecord

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
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
    companion object {
        fun newInstance(): RecordEditDialog {
            val args = Bundle()

            val fragment = RecordEditDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: SeatDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt(SeatDetailRecordActivity.DETAIL_RECORD_REVIEW_ID)
        navigateEditMethod(id ?: 0)
    }

    private fun navigateEditMethod(id: Int) {
        binding.tvRecordEdit.setOnClickListener {
            /** 수정 이동 추후 합칠 때 추가하기*/
            dismiss()
        }
        binding.tvRecordDelete.setOnClickListener {
            viewModel.removeReviewData(id)
            dismiss()
        }
    }
}