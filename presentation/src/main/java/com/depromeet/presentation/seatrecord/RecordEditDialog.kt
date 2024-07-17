package com.depromeet.presentation.seatrecord

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentRecordEditBottomSheetBinding
import com.depromeet.presentation.seatrecord.viewmodel.SeatDetailViewModel
import com.depromeet.presentation.seatrecord.viewmodel.SeatRecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordEditDialog : BindingBottomSheetDialog<FragmentRecordEditBottomSheetBinding>(
    R.layout.fragment_record_edit_bottom_sheet,
    FragmentRecordEditBottomSheetBinding::inflate
) {
    companion object {
        private const val VIEW_MODEL_TAG = "viewModelTag"

        fun newInstance(viewModelTag: String): RecordEditDialog {
            val args = Bundle()
            args.putString(VIEW_MODEL_TAG, viewModelTag)

            val fragment = RecordEditDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)

        val viewModelTag = arguments?.getString(VIEW_MODEL_TAG)
        viewModel = when (viewModelTag) {
            SeatDetailRecordActivity.SEAT_DETAIL_TAG -> ViewModelProvider(requireActivity())[SeatDetailViewModel::class.java]
            SeatRecordActivity.SEAT_RECORD_TAG -> ViewModelProvider(requireActivity())[SeatRecordViewModel::class.java]
            else -> throw IllegalArgumentException("알수 없는 뷰모델 태그")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateEditMethod()
    }

    private fun navigateEditMethod() {
        /** 1차 MVP 보류 */
//        binding.tvRecordEdit.setOnClickListener {
//            /** 수정 이동 추후 합칠 때 추가하기*/
//            dismiss()
//        }
        binding.tvRecordDelete.setOnClickListener {
            when (viewModel) {
                is SeatDetailViewModel -> (viewModel as SeatDetailViewModel).setDeleteEvent()
                is SeatRecordViewModel -> (viewModel as SeatRecordViewModel).setDeleteEvent()
                else -> throw IllegalArgumentException("알 수 없는 뷰모델")
            }
            dismiss()
        }
    }
}