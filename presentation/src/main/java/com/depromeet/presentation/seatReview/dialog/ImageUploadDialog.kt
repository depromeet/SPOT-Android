package com.depromeet.presentation.seatReview.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentUploadBottomSheetBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import com.depromeet.presentation.extension.toUri
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.UploadErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageUploadDialog : BindingBottomSheetDialog<FragmentUploadBottomSheetBinding>(
    R.layout.fragment_upload_bottom_sheet,
    FragmentUploadBottomSheetBinding::inflate,
) {

    companion object {
        private const val REQUEST_KEY = "requestKey"
        private const val SELECTED_IMAGES = "selected_images"
        private const val IMAGE_TITLE = "image"
    }

    private var permissionRequired = arrayOf(Manifest.permission.CAMERA)
    private lateinit var selectMultipleMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
        setupUserPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUploadImageMethod()
    }

    private fun initUploadImageMethod() {
        binding.layoutGallery.setOnSingleClickListener {
            selectMultipleMediaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        }
        binding.layoutTakePhoto.setOnSingleClickListener {
            if (!checkUserPermission(requireContext())) {
                activityResultLauncher.launch(permissionRequired)
            } else {
                initCameraLauncher()
            }
        }
        setupActivityResultLaunchers()
    }

    private fun setupActivityResultLaunchers() {
        selectMultipleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { uris ->
                val validUris = uris.filter { uri ->
                    initCapacityLimitDialog(uri)
                }
                if (validUris.isNotEmpty()) {
                    val uriList = validUris.map { it.toString() }
                    setFragmentResult(REQUEST_KEY, bundleOf(SELECTED_IMAGES to uriList))
                }
                dismiss()
            }

        takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.extras?.get("data")?.let { bitmap ->
                        (bitmap as? Bitmap)?.let {
                            val uri = it.toUri(requireContext(), IMAGE_TITLE)
                            if (initCapacityLimitDialog(uri)) {
                                setFragmentResult(
                                    REQUEST_KEY,
                                    bundleOf(SELECTED_IMAGES to arrayListOf(uri.toString())),
                                )
                            }
                            dismiss()
                        }
                    }
                }
            }
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

    @SuppressLint("Recycle")
    private fun initCapacityLimitDialog(uri: Uri): Boolean {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val sizeBytes = inputStream?.available() ?: 0
        val sizeMB = sizeBytes / (1024f * 1024f)
        return if (sizeMB > 15) {
            val fragment = UploadErrorDialog(
                getString(R.string.upload_error_capacity_description),
                getString(R.string.upload_error_capacity_15MB),
                getString(R.string.upload_error_discipline),
            )
            fragment.show(parentFragmentManager, fragment.tag)
            false
        } else {
            true
        }
    }

    private fun initCameraLauncher() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoLauncher.launch(takePictureIntent)
    }
}
