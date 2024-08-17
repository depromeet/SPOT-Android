package com.dpm.presentation.scheme

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.dpm.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivitySchemeBinding
import com.dpm.presentation.extension.toast

class SchemeActivity : BaseActivity<ActivitySchemeBinding>(ActivitySchemeBinding::inflate) {

    companion object {
        /**
         * 딥링크 URL 파라미터인 KEY 상수 정의
         * kakaolink://[kakao_native_key]?[key1]=[value1]&[key2]=[value2]]
         * Link(
         *     androidExecutionParams = mapOf("section" to "오렌지석", "block" to "102")
         * )
         *
         * val section = handleQueryParameter(appLinkData, "section") // 오렌지석
         * val block = handleQueryParameter(appLinkData, "block") // 102
         */
        const val EXAMPLE_KEY = "example_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntent(intent)
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            handleQueryParameter(appLinkData, EXAMPLE_KEY)
            // handlePathParameter(appLinkData)
        }
    }

    private fun handleQueryParameter(
        uri: Uri?,
        key: String
    ): String? {
        return uri?.getQueryParameter(key)
    }


    private fun handlePathParameter(
        uri: Uri?
    ) {
        uri?.lastPathSegment?.also { pathParameter ->
            Uri.parse("content://com.depromeet.spot/")
                .buildUpon()
                .appendPath(pathParameter)
                .build().also { appData ->
                    toast(appData.toString())
                }
        }
    }
}