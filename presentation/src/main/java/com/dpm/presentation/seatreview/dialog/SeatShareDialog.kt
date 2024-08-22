package com.dpm.presentation.seatreview.dialog

import android.os.Bundle
import android.view.View
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSightShareBottomSheetBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.presentation.extension.setOnSingleClickListener

class SeatShareDialog : BindingBottomSheetDialog<FragmentSightShareBottomSheetBinding>(
    R.layout.fragment_sight_share_bottom_sheet,
    FragmentSightShareBottomSheetBinding::inflate,
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentMaterialDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        binding.csbvSelectImage.setText("시야 사진이 1장 이상인 경우 선택해주세요")
    }

    private fun initEvent() {
        binding.btnYes.setOnSingleClickListener {
            // TODO : 시야 사진 공유 액티비티 이동
        }
        binding.btnNo.setOnSingleClickListener {
            dismiss()
        }
    }
}
