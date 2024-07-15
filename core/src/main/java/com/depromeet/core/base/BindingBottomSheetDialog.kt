package com.depromeet.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BindingBottomSheetDialog<B : ViewBinding>(
    @LayoutRes private val layoutRes: Int,
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B,
) : BottomSheetDialogFragment() {

    private var _binding: B? = null
    protected val binding: B
        get() = requireNotNull(_binding) { "binding object is not initialized" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun setLayoutSizeRatio(widthPercent: Float, heightPercent: Float) {
        context?.resources?.displayMetrics?.let { metrics ->
            binding.root.layoutParams.apply {
                width = ((metrics.widthPixels * widthPercent).toInt())
                height = ((metrics.heightPixels * heightPercent).toInt())
            }
        }
    }
}
