package com.depromeet.presentation.setting

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingDialogFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentConfirmDeleteDialogBinding
import com.depromeet.presentation.databinding.FragmentLogoutDialogBinding
import com.depromeet.presentation.databinding.FragmentWithdrawDialogBinding
import com.depromeet.presentation.seatrecord.viewmodel.SeatRecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WithdrawDialog : BindingDialogFragment<FragmentWithdrawDialogBinding>(
    R.layout.fragment_withdraw_dialog,
    FragmentWithdrawDialogBinding::inflate
) {
    companion object {
        private const val VIEW_MODEL_TAG = "viewModelTag"
        const val TAG = "ConfirmDialog"

        fun newInstance(viewModelTag: String): WithdrawDialog {
            val args = Bundle()
            args.putString(VIEW_MODEL_TAG, viewModelTag)

            val fragment = WithdrawDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel : SettingViewModel by activityViewModels()

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
            viewModel.withdraw()
            dismiss()
        }
        binding.btConfirmCancel.setOnClickListener {
            dismiss()
        }

    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }


}