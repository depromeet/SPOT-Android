package com.dpm.presentation.home.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentLevelDescriptionBottomSheetBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.core.state.UiState
import com.dpm.presentation.extension.toast
import com.dpm.presentation.home.adapter.LevelDescriptionAdapter
import com.dpm.presentation.home.viewmodel.HomeGuiViewModel
import timber.log.Timber

class LevelDescriptionDialog : BindingBottomSheetDialog<FragmentLevelDescriptionBottomSheetBinding>(
    R.layout.fragment_level_description_bottom_sheet,
    FragmentLevelDescriptionBottomSheetBinding::inflate
) {
    private lateinit var adapter: LevelDescriptionAdapter
    private val viewModel: HomeGuiViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserve()
    }

    private fun initView() {
        viewModel.getLevelDescription()
        adapter = LevelDescriptionAdapter()
        binding.rvLevelDescription.adapter = adapter
    }

    private fun initObserve() {
        viewModel.levelDescriptions.asLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    Timber.d("test 성공")
                    adapter.submitList(state.data)
                }

                is UiState.Loading -> {
                    Timber.d("test로딩")
                }
                is UiState.Empty -> {
                    Timber.d("test 비어있음")
                }
                is UiState.Failure -> {
                    Timber.d("test 실패")
                    toast("레벨 불러오기 실패!")
                }
            }
        }
    }
}