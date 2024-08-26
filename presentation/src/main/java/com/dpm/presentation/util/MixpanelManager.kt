package com.dpm.presentation.util

import android.content.Context
import com.depromeet.presentation.BuildConfig
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

object MixpanelManager {
    private var mixpanel: MixpanelAPI? = null

    fun initialize(context: Context) {
        if (mixpanel == null) {
            mixpanel = MixpanelAPI.getInstance(context, BuildConfig.MIXPANEL_TOKEN, false)
        }
    }

    fun track(event: String, properties: JSONObject? = null) {
        mixpanel?.track(event, properties)
    }
}
