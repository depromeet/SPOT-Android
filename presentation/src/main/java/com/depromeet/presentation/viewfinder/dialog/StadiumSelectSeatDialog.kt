package com.depromeet.presentation.viewfinder.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentStadiumSelectSeatDialogBinding

class StadiumSelectSeatDialog : BindingBottomSheetDialog<FragmentStadiumSelectSeatDialogBinding>(
    R.layout.fragment_stadium_select_seat_dialog,
    FragmentStadiumSelectSeatDialogBinding::inflate
) {
    companion object {
        const val TAG = "StadiumSelectSeatDialog"

        fun newInstance(): StadiumSelectSeatDialog {
            val args = Bundle()

            val fragment = StadiumSelectSeatDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutSizeRatio(heightPercent = 0.54f, widthPercent = 1f)

        binding.clColumnNumberDescription.setOnClickListener {
            if (binding.layoutColumnDescription.isVisible) {
                binding.layoutColumnDescription.visibility = View.GONE
                binding.ivUpDown.setImageResource(R.drawable.ic_chevron_down)
            } else {
                binding.layoutColumnDescription.visibility = View.VISIBLE
                binding.ivUpDown.setImageResource(R.drawable.ic_chevron_up)
            }
        }

        binding.btnCheckColumn.setOnClickListener {
            if (binding.clColumnNumber.isVisible) {
                binding.btnCheckColumn.setBackgroundResource(R.drawable.ic_check)
                binding.clColumnNumber.visibility = View.INVISIBLE
                binding.clOnlyColumn.visibility = View.VISIBLE
                binding.etColumn.setText("")
                binding.etNumber.setText("")
            } else {
                binding.btnCheckColumn.setBackgroundResource(R.drawable.ic_uncheck)
                binding.clOnlyColumn.visibility = View.INVISIBLE
                binding.clColumnNumber.visibility = View.VISIBLE
                binding.etOnlyColumn.setText("")
            }
            binding.btnAdapt.isEnabled = false
            binding.tvWarning.visibility = View.INVISIBLE
        }

        binding.btnAdapt.setOnClickListener {
            if (binding.clColumnNumber.isVisible) {
                if (binding.etColumn.text.toString() == "444") {
                    binding.tvWarning.visibility = View.VISIBLE
                    binding.tvWarning.text = "존재하지 않는 열이에요"
                }

                if (binding.etNumber.text.toString() == "444") {
                    binding.tvWarning.visibility = View.VISIBLE
                    binding.tvWarning.text = "존재하지 않는 번이에요"
                }
            }

            if (binding.clOnlyColumn.isVisible) {
                if (binding.etOnlyColumn.text.toString() == "444") {
                    binding.tvWarning.visibility = View.VISIBLE
                    binding.tvWarning.text = "존재하지 않는 열이에요"
                }
            }

        }

        binding.etOnlyColumn.addTextChangedListener { editText ->
            binding.tvWarning.visibility = View.INVISIBLE
            if (binding.clOnlyColumn.isVisible) {
                binding.btnAdapt.isEnabled = editText?.isNotEmpty() == true
            }
        }

        binding.etColumn.addTextChangedListener { editText ->
            binding.tvWarning.visibility = View.INVISIBLE
            if (binding.clColumnNumber.isVisible) {
                if (binding.etNumber.text.isNotEmpty()) {
                    binding.btnAdapt.isEnabled = editText?.isNotEmpty() == true
                }
            }
        }

        binding.etNumber.addTextChangedListener { editText ->
            binding.tvWarning.visibility = View.INVISIBLE
            if (binding.clColumnNumber.isVisible) {
                if (binding.etColumn.text.isNotEmpty()) {
                    binding.btnAdapt.isEnabled = editText?.isNotEmpty() == true
                }
            }
        }
    }
}