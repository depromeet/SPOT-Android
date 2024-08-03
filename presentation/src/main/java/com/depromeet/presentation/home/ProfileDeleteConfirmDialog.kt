package com.depromeet.presentation.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingDialogFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentConfirmDeleteDialogBinding
import com.depromeet.presentation.home.viewmodel.ProfileEditViewModel
import com.depromeet.presentation.seatrecord.ConfirmDeleteDialog

class ProfileDeleteConfirmDialog : BindingDialogFragment<FragmentConfirmDeleteDialogBinding>(
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

    private val viewModel: ProfileEditViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvConfirmDiscipline.text = getString(R.string.home_profile_image_delete)
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val params = attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params
        }

        binding.btConfirmCheck.setOnClickListener {
            viewModel.deleteProfileImage()
            dismiss()
        }
        binding.btConfirmCancel.setOnClickListener {
            dismiss()
        }

    }

}