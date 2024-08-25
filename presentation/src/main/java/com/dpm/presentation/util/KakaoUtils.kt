package com.dpm.presentation.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.DefaultTemplate
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link

val mockDefaultFeed = FeedTemplate(
    content = Content(
        title = "오늘의 디저트",
        description = "#케익 #딸기 #삼평동 #카페 #분위기 #소개팅",
        imageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
        link = Link(
            webUrl = "https://play.google.com",
            mobileWebUrl = "https://play.google.com"
        )
    ),
    buttons = listOf(
        Button(
            "앱으로 보기",
            Link(
                androidExecutionParams = mapOf("key1" to "value1", "key2" to "value2")
            )
        )
    )
)

fun seatFeed(
    title: String,
    description: String,
    imageUrl: String,
    queryParams: Map<String, String>
) = FeedTemplate(
    content = Content(
        title = title,
        description = description,
        imageUrl = imageUrl,
        link = Link()
    ),
    buttons = listOf(
        Button(
            title = "SPOT! 앱에서 열기",
            link = Link(
                androidExecutionParams = queryParams
            )
        )
    )
)

class KakaoUtils() {
    fun share(
        context: Context,
        template: DefaultTemplate,
        onSuccess: (intent: Intent) -> Unit = {},
        onFailure: (Throwable) -> Unit = {}
    ) {
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            // 카카오톡으로 카카오톡 공유 가능
            ShareClient.instance.shareDefault(context, template) { sharingResult, error ->
                if (error != null) {
                    onFailure(error)
                } else if (sharingResult != null) {
                    onSuccess(sharingResult.intent)
                }
            }
        } else { // 카카오톡 미설치: 웹 공유 사용 권장
            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(template)

            // 1. CustomTabsServiceConnection 지원 브라우저 열기
            // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
            try {
                KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
            } catch (e: UnsupportedOperationException) {
                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
            }

            // 2. CustomTabsServiceConnection 미지원 브라우저 열기
            // ex) 다음, 네이버 등
            try {
                KakaoCustomTabsClient.open(context, sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
            }
        }
    }
}