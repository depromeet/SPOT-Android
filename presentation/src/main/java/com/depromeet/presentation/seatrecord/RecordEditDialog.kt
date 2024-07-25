package com.depromeet.presentation.seatrecord

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentRecordEditBottomSheetBinding
import com.depromeet.presentation.seatrecord.viewmodel.SeatRecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordEditDialog : BindingBottomSheetDialog<FragmentRecordEditBottomSheetBinding>(
    R.layout.fragment_record_edit_bottom_sheet,
    FragmentRecordEditBottomSheetBinding::inflate
) {
    companion object {
        private const val VIEW_MODEL_TAG = "viewModelTag"
        const val TAG = "RecordEditDialog"

        fun newInstance(viewModelTag: String): RecordEditDialog {
            val args = Bundle()
            args.putString(VIEW_MODEL_TAG, viewModelTag)

            val fragment = RecordEditDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: SeatRecordViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateEditMethod()
    }

    private fun navigateEditMethod() {
        binding.tvRecordDelete.setOnClickListener {
            viewModel.setDeleteEvent()
            dismiss()
        }
    }
}