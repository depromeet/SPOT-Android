package com.depromeet.presentation.seatrecord

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.depromeet.core.base.BindingDialogFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentConfirmDeleteDialogBinding
import com.depromeet.presentation.seatrecord.viewmodel.SeatDetailViewModel
import com.depromeet.presentation.seatrecord.viewmodel.SeatRecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmDeleteDialog : BindingDialogFragment<FragmentConfirmDeleteDialogBinding>(
    R.layout.fragment_confirm_delete_dialog,
    FragmentConfirmDeleteDialogBinding::inflate
) {
    companion object {
        private const val VIEW_MODEL_TAG = "viewModelTag"
        const val TAG = "ConfirmDialog"

        fun newInstance(viewModelTag: String): ConfirmDeleteDialog {
            val args = Bundle()
            args.putString(VIEW_MODEL_TAG, viewModelTag)

            val fragment = ConfirmDeleteDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentDialogFragment)

        val viewModelTag = arguments?.getString(VIEW_MODEL_TAG)
        viewModel = when (viewModelTag) {
            SeatDetailRecordActivity.SEAT_DETAIL_TAG -> ViewModelProvider(requireActivity())[SeatDetailViewModel::class.java]
            SeatRecordActivity.SEAT_RECORD_TAG -> ViewModelProvider(requireActivity())[SeatRecordViewModel::class.java]
            else -> throw IllegalArgumentException("알수 없는 뷰모델 태그")
        }
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
            when (viewModel) {
                is SeatDetailViewModel -> (viewModel as SeatDetailViewModel).removeReviewData()
                is SeatRecordViewModel -> (viewModel as SeatRecordViewModel).removeReviewData()
                else -> throw IllegalArgumentException("알 수 없는 뷰모델")
            }
            dismiss()
        }
        binding.btConfirmCancel.setOnClickListener {
            dismiss()
        }

    }


}