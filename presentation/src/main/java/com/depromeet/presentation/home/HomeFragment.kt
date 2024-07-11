package com.depromeet.presentation.home

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import coil.load
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(
    layoutResId = R.layout.fragment_home,
    bindingInflater = FragmentHomeBinding::inflate
) {
    private val viewModel: HomeViewModel by viewModels()
    private val sightList: List<View> by lazy {
        listOf(
            binding.clHomeNoRecord,
            binding.clHomeOneRecord,
            binding.clHomeTwoRecord,
            binding.clHomeThreeRecord
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivHomeProfile.setOnClickListener { navigateToProfileEditFragment() }
        binding.ibHomeEdit.setOnClickListener { navigateToProfileEditFragment() }
        binding.clMainSight.clipToOutline = true

        viewModel.uiState.asLiveData().observe(viewLifecycleOwner) { state ->
            updateUi(state)
        }

        viewModel.getInformation()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(state: HomeUiState) {
        with(binding) {
            val profile = state.profile
            val recentSight = state.recentSight

            tvHomeLevel.text = "Lv.${profile.level} ${profile.title}"
            setSpannableString(profile.nickName, profile.writeCount)
            ivHomeCheerTeam.load(profile.cheerTeam)
            tvHomeRecentRecordName.text = recentSight.location
            tvHomeRecentRecordDate.text = recentSight.date


            sightList.forEachIndexed { index, view ->
                view.visibility =
                    if (index == recentSight.photoList.size) View.VISIBLE else View.GONE
            }

            when (recentSight.photoList.size) {
                0 -> {
                    tvHomeRecentRecordDate.visibility = View.GONE
                    tvHomeRecentRecordName.visibility = View.GONE
                }

                1 -> {
                    ivHomeRecentOneRecord1.loadAndClip(recentSight.photoList[0])
                    tvHomeRecentOneSection.text = recentSight.section
                }

                2 -> {
                    ivHomeRecentTwoRecord1.loadAndClip(recentSight.photoList[0])
                    ivHomeRecentTwoRecord2.loadAndClip(recentSight.photoList[1])
                    tvHomeRecentTwoSection.text = recentSight.section
                }

                3 -> {
                    ivHomeRecentThreeRecord1.loadAndClip(recentSight.photoList[0])
                    ivHomeRecentThreeRecord2.loadAndClip(recentSight.photoList[1])
                    ivHomeRecentThreeRecord3.loadAndClip(recentSight.photoList[2])
                    tvHomeRecentThreeSection.text = recentSight.section
                }

                else -> {}
            }
        }
    }

    private fun ImageView.loadAndClip(imageUrl: String) {
        this.load(imageUrl)
        this.clipToOutline = true
    }

    @SuppressLint("CommitTransaction")
    private fun navigateToProfileEditFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fcv_home, ProfileEditFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun setSpannableString(
        nickName: String,
        writeCount: Int,
    ) {
        val text = "${nickName}님, 지금까지\n시야후기를\n${writeCount}번 작성했어요!"
        val spannableBuilder = SpannableStringBuilder(text)

        val startIndexNickname = 0
        val endIndexNickname = nickName.length
        applyBoldAndSizeSpan(spannableBuilder, startIndexNickname, endIndexNickname)

        val startIndexWriteCount = text.indexOf(writeCount.toString())
        val endIndexWriteCount = startIndexWriteCount + writeCount.toString().length
        applyBoldAndSizeSpan(spannableBuilder, startIndexWriteCount, endIndexWriteCount)

        binding.tvHomeSightChance.text = spannableBuilder
    }

    private fun applyBoldAndSizeSpan(
        spannableBuilder: SpannableStringBuilder,
        startIndex: Int,
        endIndex: Int,
    ) {
        spannableBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableBuilder.setSpan(
            RelativeSizeSpan(1.3f),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }


}