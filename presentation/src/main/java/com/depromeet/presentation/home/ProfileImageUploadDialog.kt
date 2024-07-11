package com.depromeet.presentation.home

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentProfileEditBottomSheetBinding
import com.depromeet.presentation.home.viewmodel.ProfileEditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileImageUploadDialog() : BindingBottomSheetDialog<FragmentProfileEditBottomSheetBinding>(
    R.layout.fragment_profile_edit_bottom_sheet,
    FragmentProfileEditBottomSheetBinding::inflate
) {

    private val viewModel: ProfileEditViewModel by activityViewModels()
    private lateinit var pickSingleImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
        setupPickSingleImageLauncher()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateEditMethod()
    }

    private fun navigateEditMethod() {
        binding.tvProfileEditClose.setOnClickListener {
            dismiss()
        }
        binding.tvProfileDelete.setOnClickListener {
            //삭제 요청
        }
        binding.tvProfileSelectAlbum.setOnClickListener {
            pickSingleImageLauncher.launch("image/*")
        }
    }

    private fun setupPickSingleImageLauncher() {
        pickSingleImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    viewModel.setProfileImage(uri.toString())
                    dismiss()
                }
            }
    }


}