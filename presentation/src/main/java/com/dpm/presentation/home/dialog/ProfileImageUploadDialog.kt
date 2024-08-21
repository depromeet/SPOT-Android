package com.dpm.presentation.home.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentProfileEditBottomSheetBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.presentation.extension.toast
import com.dpm.presentation.home.viewmodel.ProfileEditViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ProfileImageUploadDialog() : BindingBottomSheetDialog<FragmentProfileEditBottomSheetBinding>(
    R.layout.fragment_profile_edit_bottom_sheet,
    FragmentProfileEditBottomSheetBinding::inflate,
) {

    private val viewModel: ProfileEditViewModel by activityViewModels()
    private var permissionRequired = arrayOf(Manifest.permission.CAMERA)
    private lateinit var pickSingleImageLauncher: ActivityResultLauncher<String>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in permissionRequired && !it.value) {
                    permissionGranted = false
                }
            }
            if (!permissionGranted) {
                toast("권한 요청이 거부되었습니다.")
            } else {
                initCameraLauncher()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
        setupUserPermission()
        setupPickSingleImageLauncher()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateEditMethod()
    }

    private fun navigateEditMethod() {
        with(binding) {
            clProfileEditCamera.setOnClickListener {
                if (!checkUserPermission(requireContext())) {
                    activityResultLauncher.launch(permissionRequired)
                } else {
                    initCameraLauncher()
                }
            }
            clProfileEditGallery.setOnClickListener {
                pickSingleImageLauncher.launch("image/*")
            }
            clProfileEditRemove.setOnClickListener {
                ProfileDeleteConfirmDialog().show(parentFragmentManager,
                    ProfileDeleteConfirmDialog.TAG
                )
                dismiss()
            }
        }
    }

    private fun setupPickSingleImageLauncher() {
        pickSingleImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    handleSelectedImage(uri)
                }
            }

        takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    imageUri?.let { uri ->
                        handleSelectedImage(uri)
                    }
                }
            }
    }

    private fun initCameraLauncher() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imageFile = createImageFile()
        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imageFile,
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        takePhotoLauncher.launch(takePictureIntent)
    }

    private fun createImageFile(): File {
        val storageDir: File? = requireContext().getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${System.currentTimeMillis()}_",
            ".jpg",
            storageDir,
        )
    }

    private fun setupUserPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val permissionList = permissionRequired.toMutableList()
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permissionRequired = permissionList.toTypedArray()
        }
    }

    private fun checkUserPermission(context: Context) = permissionRequired.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
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
