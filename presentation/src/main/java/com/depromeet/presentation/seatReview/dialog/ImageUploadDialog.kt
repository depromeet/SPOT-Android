package com.depromeet.presentation.seatReview.dialog

import android.Manifest
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
import android.widget.Toast
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
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

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

    private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)
    private lateinit var selectMultipleMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
        setupActivityResultLaunchers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUploadMethod()

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val permissionList = PERMISSIONS_REQUIRED.toMutableList()
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            PERMISSIONS_REQUIRED = permissionList.toTypedArray()
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in PERMISSIONS_REQUIRED && !it.value) {
                    permissionGranted = false
                }
            }
            if (!permissionGranted) {
                Toast.makeText(context, "권한 요청이 거부되었습니다.", Toast.LENGTH_LONG).show()
            } else {
                navigateToCamera()
            }
        }

    private fun setupActivityResultLaunchers() {
        selectMultipleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { uris ->
                val uriList = uris.map { it.toString() }
                setFragmentResult(REQUEST_KEY, bundleOf(SELECTED_IMAGES to uriList))
                dismiss()
            }

        takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.extras?.get("data")?.let { bitmap ->
                        (bitmap as? Bitmap)?.let {
                            val uri = it.toUri(requireContext())
                            setFragmentResult(
                                REQUEST_KEY,
                                bundleOf(SELECTED_IMAGES to arrayListOf(uri.toString())),
                            )
                            dismiss()
                        }
                    }
                }
            }
    }

    private fun setupUploadMethod() {
        with(binding) {
            layoutGallery.setOnSingleClickListener {
                selectMultipleMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            layoutTakePhoto.setOnSingleClickListener {
                if (!hasPermissions(requireContext())) {
                    activityResultLauncher.launch(PERMISSIONS_REQUIRED)
                } else {
                    navigateToCamera()
                }
            }
        }
    }

    private fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun navigateToCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoLauncher.launch(takePictureIntent)
    }

    private fun Bitmap.toUri(context: Context): Uri {
        val bytes = ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG, 100, this)
        }
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, this, IMAGE_TITLE, null)
        return Uri.parse(path)
    }
}