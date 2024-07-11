package com.depromeet.presentation.viewfinder.web

import android.webkit.JavascriptInterface

class AndroidBridge(
    private val callback: (fromWeb: String) -> Unit
) {
    @JavascriptInterface
    fun textFromWeb(fromWeb: String) {
        callback(fromWeb)
    }
}