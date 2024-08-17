package com.dpm.presentation.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentUploadErrorDialogBinding
import com.dpm.core.base.BindingDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadErrorDialog(
    private val description: String,
    private val capacity: String? = null,
    private val discipline: String,
) : BindingDialogFragment<FragmentUploadErrorDialogBinding>(
    layoutResId = R.layout.fragment_upload_error_dialog,
    bindingInflater = FragmentUploadErrorDialogBinding::inflate,
) {

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
            tvErrorDescription.text = description
            tvErrorCapacity.text = capacity
            tvErrorDiscipline.text = discipline
            btErrorCheck.setOnClickListener {
                dismiss()
            }
        }
    }
}
