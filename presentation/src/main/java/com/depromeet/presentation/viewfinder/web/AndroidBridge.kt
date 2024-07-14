package com.depromeet.presentation.viewfinder.web

import android.webkit.JavascriptInterface

class AndroidBridge(
    private val callback: (sectionId: String) -> Unit
) {
    companion object {
        const val JAVASCRIPT_OBJ = "javascript_obj"
        const val INJECT_STADIUM_BLOCK_NUMBER =
            "javascript: window.androidObj.getStadiumBlockNumber = function(sectionId) { $JAVASCRIPT_OBJ.getStadiumBlockNumber(sectionId) }"
    }

    @JavascriptInterface
    fun getStadiumBlockNumber(sectionId: String) {
        callback(sectionId)
    }
}