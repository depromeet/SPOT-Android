package com.depromeet.presentation.home

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityHomeBinding
import com.depromeet.presentation.extension.loadAndClip
import com.depromeet.presentation.home.viewmodel.HomeUiState
import com.depromeet.presentation.home.viewmodel.HomeViewModel
import com.depromeet.presentation.util.applyBoldAndSizeSpan
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(
    ActivityHomeBinding::inflate
) {
    companion object {
        const val PROFILE_NAME = "profile_name"
        const val PROFILE_IMAGE = "profile_image"
        const val PROFILE_CHEER_TEAM = "profile_cheer_team"
    }

    private val viewModel: HomeViewModel by viewModels()
    private val sightList: List<View> by lazy {
        listOf(
            binding.clHomeNoRecord,
            binding.clHomeOneRecord,
            binding.clHomeTwoRecord,
            binding.clHomeThreeRecord
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivHomeProfile.setOnClickListener { navigateToProfileEditActivity() }
        binding.ibHomeEdit.setOnClickListener { navigateToProfileEditActivity() }
        binding.clMainSight.clipToOutline = true

        viewModel.uiState.asLiveData().observe(this) { state ->
            updateUi(state)
        }

        viewModel.getInformation()
    }


    private fun updateUi(state: HomeUiState) {
        with(binding) {
            val profile = state.profile
            val recentSight = state.recentSight

            "Lv.${profile.level} ${profile.title}".also { tvHomeLevel.text = it }
            setSpannableString(profile.nickName, profile.writeCount)
            ivHomeCheerTeam.load(profile.teamImage)
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

    private fun navigateToProfileEditActivity() {
        Intent(this, ProfileEditActivity::class.java).apply {
            with(viewModel.uiState.value.profile) {
                putExtra(PROFILE_NAME, this.nickName)
                putExtra(PROFILE_IMAGE, this.image)
                putExtra(PROFILE_CHEER_TEAM, this.teamId)
            }
        }.let(::startActivity)
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

}