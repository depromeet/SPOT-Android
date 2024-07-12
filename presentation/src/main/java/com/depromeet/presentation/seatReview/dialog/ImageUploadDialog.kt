package com.depromeet.presentation.seatReview.dialog

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentUploadBottomSheetBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import com.depromeet.presentation.seatReview.ReviewViewModel
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

    private val viewModel: ReviewViewModel by activityViewModels()
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
        binding.apply {
            layoutGallery.setOnSingleClickListener {
                selectMultipleMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            layoutTakePhoto.setOnSingleClickListener {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePhotoLauncher.launch(takePictureIntent)
            }
        }
    }

    private fun Bitmap.toUri(context: Context): Uri {
        val bytes = ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG, 100, this)
        }
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, this, IMAGE_TITLE, null)
        return Uri.parse(path)
    }
}
