package com.depromeet.presentation.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySettingBinding
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.login.ui.SignUpActivity
import com.depromeet.presentation.seatrecord.ConfirmDeleteDialog
import com.depromeet.presentation.seatrecord.SeatDetailRecordFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(
    ActivitySettingBinding::inflate
){
    private val viewModel : SettingViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        initObserver()
    }

    private fun initObserver() {
        viewModel.logoutEvent.asLiveData().observe(this) {
            toast("로그아웃 되었습니다.")
            Intent(this, SignUpActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
            }
        }
    }

    private fun initView() {
        binding.tvSettingAppVersion.text = packageManager.getPackageInfo(this.packageName, 0).versionName
    }

    private fun initEvent() = with(binding){
        appbarSetting.setNavigationOnClickListener {
            finish()
        }

        clSettingFeedback.setOnClickListener {
            Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "https://docs.google.com/forms/d/1i-qAtounQY3J7h92c7WVpoGlylhQZSFDf0VzDw77xBw/viewform?edit_requested=true"
                )
            ).apply {
                startActivity(this)
            }
        }

        tvSettingLoginInfo.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.fcv_setting, LoginInfoFragment())
                addToBackStack(null)
            }
        }

        tvSettingReport.setOnClickListener {
            Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "https://docs.google.com/forms/d/1lzGJb8YiC35l4OslLGOJkrNGfXsnVBMGg5d_0ttoZK4/viewform?edit_requested=true"
                )
            ).apply {
                startActivity(this)
            }
        }

        tvSettingAppVersionTitle.setOnClickListener {

        }

        tvSettingLicense.setOnClickListener {

        }

        // 약관 정채 Fragment 화면으로
        tvSettingTerms.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.fcv_setting, TermsFragment())
                addToBackStack(null)
            }
        }

        tvSettingLogout.setOnClickListener {
            LogoutDialog.newInstance("tag")
                .show(supportFragmentManager, "tag")
        }
    }
}