package com.depromeet.presentation.home

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
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
    FragmentProfileEditBottomSheetBinding::inflate,
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
            viewModel.deleteProfileImage()
            dismiss()
        }
        binding.tvProfileSelectAlbum.setOnClickListener {
            pickSingleImageLauncher.launch("image/*")
        }
    }

    private fun setupPickSingleImageLauncher() {
        pickSingleImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    handleSelectedImage(uri)
                }
            }
    }

    @SuppressLint("Recycle")
    private fun handleSelectedImage(uri: Uri) {
        requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
            val sizeBytes = inputStream.available()
            val sizeMB = sizeBytes / (1024f * 1024f)

            if (sizeMB > 5) {
                val fragment = UploadErrorDialog(
                    getString(R.string.upload_error_capacity_description),
                    getString(R.string.upload_error_capacity_5MB),
                    getString(R.string.upload_error_discipline),
                )
                fragment.show(parentFragmentManager, fragment.tag)
                dismiss()
            } else {
                val fileExtension = getFileExtension(uri)
                if (fileExtension != "png" && fileExtension != "jpeg" && fileExtension != "jpg") {
                    val fragment = UploadErrorDialog(
                        getString(R.string.upload_error_extension_description),
                        null,
                        getString(R.string.upload_error_extension_photo),
                    )
                    fragment.show(parentFragmentManager, fragment.tag)
                    dismiss()
                    return@use
                }
                val byteArray = inputStream.readBytes()
                viewModel.setProfileImagePresigned(byteArray, fileExtension)
                viewModel.setProfileImage(uri.toString())
                dismiss()
            }
        }
    }

    private fun getFileExtension(uri: Uri): String {
        val mimeType = requireContext().contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: ""
    }
}
