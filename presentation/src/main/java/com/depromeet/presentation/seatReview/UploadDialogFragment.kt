package com.depromeet.presentation.seatReview

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
import androidx.fragment.app.setFragmentResult
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentUploadBottomSheetBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import java.io.ByteArrayOutputStream

class UploadDialogFragment : BindingBottomSheetDialog<FragmentUploadBottomSheetBinding>(
    R.layout.fragment_upload_bottom_sheet,
    FragmentUploadBottomSheetBinding::inflate,
) {

    companion object {
        private const val REQUEST_KEY = "requestKey"
        private const val SELECTED_IMAGES = "selected_images"
    }

    private lateinit var pickMultipleMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPickMultipleMediaLauncher()
        setupTakePhotoLauncher()
    }

    private fun setupPickMultipleMediaLauncher() {
        pickMultipleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { uris ->
                val uriList = uris.map { it.toString() }
                val bundle = Bundle().apply {
                    putStringArrayList(SELECTED_IMAGES, ArrayList(uriList))
                }
                setFragmentResult(REQUEST_KEY, bundle)
                dismiss()
            }
    }

    private fun setupTakePhotoLauncher() {
        takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    val bitmap = data?.extras?.get("data") as Bitmap?
                    bitmap?.let {
                        val uri = getImageUri(requireContext(), it)
                        val bundle = Bundle().apply {
                            putStringArrayList(SELECTED_IMAGES, arrayListOf(uri.toString()))
                        }
                        setFragmentResult(REQUEST_KEY, bundle)
                        dismiss()
                    }
                }
            }
    }

    private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Image", null)
        return Uri.parse(path)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGallery.setOnSingleClickListener {
            pickMultipleMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.btnTakePhoto.setOnSingleClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoLauncher.launch(takePictureIntent)
        }
    }
}
