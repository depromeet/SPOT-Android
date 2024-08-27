package com.dpm.spot

import android.app.Application
import com.dpm.presentation.util.MixpanelManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(false)
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        } else {
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
            MixpanelManager.initialize(this)
        }
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        // TODO : 데이터 트래킹 Dev 작동 확인 -> release 모드 실행
//        MixpanelManager.initialize(this)
    }
}
