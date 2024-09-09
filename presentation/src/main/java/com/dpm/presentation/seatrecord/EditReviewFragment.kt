package com.dpm.presentation.seatrecord

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.MimeTypeMap
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentEditReviewBinding
import com.dpm.core.base.BindingFragment
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotImageSnackBar
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.presentation.extension.loadAndClip
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.global.LoadingDialog
import com.dpm.presentation.seatrecord.dialog.EditDatePickerDialog
import com.dpm.presentation.seatrecord.dialog.EditReviewMySeatDialog
import com.dpm.presentation.seatrecord.dialog.EditSelectSeatDialog
import com.dpm.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.dpm.presentation.seatreview.dialog.main.ImageUploadDialog
import com.dpm.presentation.util.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileNotFoundException
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class EditReviewFragment : BindingFragment<FragmentEditReviewBinding>(
    R.layout.fragment_edit_review,
    FragmentEditReviewBinding::inflate
) {
    companion object {
        private const val MAX_SELECTED_IMAGES = 3
        const val EDIT_REVIEW_TAG = "editReview"
        private const val IMAGE_UPLOAD_DIALOG = "ImageUploadDialog"
        private const val SELECT_SEAT_DIALOG = "SelectSeatDialog"
        private const val REVIEW_MY_SEAT_DIALOG = "ReviewMySeatDialog"
    }

    @Inject
    lateinit var s3Url: String

    private val viewModel: SeatRecordViewModel by activityViewModels()
    private lateinit var loadingDialog : LoadingDialog

    private val selectedImage: List<ImageView> by lazy {
        listOf(
            binding.ivFirstImage,
            binding.ivSecondImage,
            binding.ivThirdImage
        )
    }
    private val selectedImageLayout: List<FrameLayout> by lazy {
        listOf(
            binding.layoutFirstImage,
            binding.layoutSecondImage,
            binding.layoutThirdImage
        )
    }
    private val removeButtons: List<ImageView> by lazy {
        listOf(
            binding.ivRemoveFirstImage,
            binding.ivRemoveSecondImage,
            binding.ivRemoveThirdImage
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
        initObserver()
    }


    private fun initView() {
        initMethodNaming(viewModel.currentReviewState.value)
        loadingDialog = LoadingDialog(requireContext())
    }

    private fun initEvent() = with(binding) {
        root.setOnClickListener {
            return@setOnClickListener
        }
        btnBack.setOnSingleClickListener {
            removeFragment()
        }
        initDatePickerDialogEvent()
        initUploadEvent()
        initEventRemoveButton()
        initSeatSelect()
        initReviewSelect()
        initUploadButton()
        onBackPressed()
    }

    private fun initObserver() {
        viewModel.editReview.asLiveData().observe(viewLifecycleOwner) {
            setDate(it.date)
            setImages(it.images.map { it.url })
            setSeatName(it.formattedSeatName())
            setCountReview(it)
            checkUploadStatus(it)
        }
        observeUploadImagesCount()
        observeUploadEditReview()
    }

    private fun initDatePickerDialogEvent() {
        binding.layoutDatePicker.setOnClickListener {
            EditDatePickerDialog().show(parentFragmentManager, EditDatePickerDialog.TAG)
        }
    }

    private fun initUploadEvent() {
        binding.llAddImage.setOnSingleClickListener {
            imageUploadResultHandler()
        }
        binding.btnAddImage.setOnSingleClickListener {
            imageUploadResultHandler()
        }
    }

    private fun imageUploadResultHandler() {
        val imageUploadDialog = ImageUploadDialog().apply {
            setOnActivityResultHandler { selectedImages ->
                addSelectedImages(selectedImages)
            }
        }
        imageUploadDialog.show(parentFragmentManager, IMAGE_UPLOAD_DIALOG)
    }

    private fun initMethodNaming(reviewType: SeatRecordViewModel.ReviewType) {
        when (reviewType) {
            SeatRecordViewModel.ReviewType.SEAT_REVIEW -> {
                binding.tvTitle.text = "좌석의 시야를 공유해보세요"
                "야구장 시야 사진을\n올려주세요".also { binding.tvAddImage.text = it }
                binding.tvReviewMySeat.text = "내 시야 후기"
            }

            SeatRecordViewModel.ReviewType.INTUITIVE_REVIEW -> {
                binding.tvTitle.text = "경기의 순간을 간직해보세요"
                "직관후기 사진을\n올려주세요".also { binding.tvAddImage.text = it }
                binding.tvReviewMySeat.text = "내 직관 후기"
            }
        }
    }

    private fun setDate(date: String) {
        binding.tvDate.text = CalendarUtil.getFormattedDotDate(date)
    }

    private fun setImages(images: List<String>) {
        binding.tvImageCount.text = images.size.toString()
        if (images.isEmpty()) {
            binding.llAddImage.visibility = VISIBLE
            selectedImageLayout.forEach { it.visibility = GONE }
        } else {
            when (images.size) {
                MAX_SELECTED_IMAGES -> binding.layoutAddImageButton.visibility = GONE
                else -> binding.layoutAddImageButton.visibility = VISIBLE
            }
            binding.llAddImage.visibility = GONE
            images.forEachIndexed { index, image ->
                selectedImage[index].loadAndClip(image)
                selectedImageLayout[index].visibility = VISIBLE
            }
            for (i in images.size until selectedImage.size) {
                selectedImageLayout[i].visibility = GONE
            }
        }

    }

    private fun setSeatName(seatName: String) {
        binding.tvSeatName.text = seatName
    }

    private fun setCountReview(data: ResponseMySeatRecord.ReviewResponse) {
        with(binding) {
            val keyWordSize = data.keywords.size
            layoutReviewNumber.visibility = when (keyWordSize) {
                0 -> GONE
                else -> VISIBLE
            }
            tvReviewCount.text = keyWordSize.toString()
        }
    }

    private fun checkUploadStatus(data: ResponseMySeatRecord.ReviewResponse) {
        val isImagesFilled = data.images.isNotEmpty()
        with(binding.tvUploadBtn) {
            if (isImagesFilled) {
                setBackgroundResource(R.drawable.rect_action_enabled_fill_8)
            } else {
                setBackgroundResource(R.drawable.rect_action_disabled_fill_8)
            }
        }

    }

    private fun initEventRemoveButton() {
        removeButtons.forEachIndexed { index, button ->
            button.setOnSingleClickListener {
                viewModel.removeEditImage(index)
            }
        }
    }

    private fun initUploadButton() = with(binding) {
        tvUploadBtn.setOnSingleClickListener {
            val images = viewModel.editReview.value.images
            when {
                /** 등록과 달리 수정은 사진을 제외(모두 삭제하는 경우)하고 기본값들이 있음 */
                images.isEmpty() -> {
                    makeSpotImageAppbar("사진을 등록해주세요")
                }

                else -> {
                    showLoading()
                    images.forEachIndexed { index, image ->
                        if (image.url.contains(s3Url)) {
                            viewModel.updatePresignedUrl(index, image.url)
                        } else {
                            val imageUri = Uri.parse(image.url)
                            val fileExtension = getFileExtension(requireContext(), imageUri)
                            val imageData = readImageData(requireContext(), imageUri)
                            if (imageData != null) {
                                image.url
                                viewModel.requestPresignedUrl(index, fileExtension, imageData)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    removeFragment()
                }
            })
    }

    private fun removeFragment() {
        parentFragmentManager.popBackStack()
    }


    private fun addSelectedImages(newImageUris: List<String>) {
        val newSelectedImage: MutableList<String> =
            if (viewModel.editReview.value.images.size + newImageUris.size > MAX_SELECTED_IMAGES) {
                makeSpotImageAppbar("사진은 최대 3장 선택할 수 있어요")
                newImageUris.take(MAX_SELECTED_IMAGES - viewModel.editReview.value.images.size)
                    .toMutableList()
            } else {
                newImageUris.toMutableList()
            }
        viewModel.addEditSelectedImages(newSelectedImage)
    }

    private fun initSeatSelect() {
        binding.layoutSeatSelection.setOnSingleClickListener {
            EditSelectSeatDialog().show(parentFragmentManager, SELECT_SEAT_DIALOG)
        }
    }


    private fun initReviewSelect() {
        binding.layoutSeatReview.setOnSingleClickListener {
            EditReviewMySeatDialog().show(parentFragmentManager, REVIEW_MY_SEAT_DIALOG)
        }
    }

    private fun observeUploadImagesCount() {
        viewModel.uploadImageCount.asLiveData().observe(viewLifecycleOwner) { cnt ->
            if (viewModel.editReview.value.images.size == cnt) {
                viewModel.putEditReview()
            }
        }
    }

    private fun observeUploadEditReview() {
        viewModel.putReviewState.asLiveData().observe(viewLifecycleOwner) { state ->
            //TODO : 로딩 다이얼로그 처리 및 추후 처리 방안 설정
            when (state) {
                is UiState.Success -> {
                    makeSpotImageAppbar("게시물 수정 성공!")
                    dismissLoading()
                    removeFragment()
                }

                is UiState.Failure -> {
                    makeSpotImageAppbar("게시물 수정 실패..")
                    dismissLoading()
                }
                else -> {}
            }
        }
    }


    private fun makeSpotImageAppbar(message: String) {
        SpotImageSnackBar.make(
            view = binding.root.rootView,
            message = message,
            messageColor = com.depromeet.designsystem.R.color.color_foreground_white,
            icon = com.depromeet.designsystem.R.drawable.ic_alert_circle,
            iconColor = com.depromeet.designsystem.R.color.color_error_secondary,
            marginBottom = 96
        ).show()
    }

    private fun getFileExtension(context: Context, uri: Uri): String {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) } ?: ""
    }

    private fun getFileInputStream(context: Context, uri: Uri): InputStream? {
        return try {
            context.contentResolver.openInputStream(uri)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    private fun readImageData(context: Context, uri: Uri): ByteArray? {
        val inputStream = getFileInputStream(context, uri)
        return inputStream?.use { it.readBytes() }
    }

    private fun showLoading(){
        loadingDialog.show()
    }

    private fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissLoading()
    }

}