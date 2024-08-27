package com.dpm.presentation.util

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController
import androidx.annotation.ColorRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

class Utils(
    private val context: Context
) {
    /**
     * 네비게이션 하단 바 높이
     */
    val navigationHeight = context.resources.getIdentifier("navigation_bar_height","dimen","android").let { context.resources.getDimensionPixelSize(it) }

    fun restartApp(toastMsg: String?) {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val componentName = packageManager.getLaunchIntentForPackage(packageName)?.component
        Intent.makeRestartActivityTask(componentName).apply {
            if (toastMsg != null) putExtra("TOAST_MSG", toastMsg)
            context.startActivity(this)
        }
        Runtime.getRuntime().exit(0)
    }

    fun setStatusBarColor(
        window: Window,
        @ColorRes color: Int
    ) {
        window.statusBarColor = context.getColor(color)
    }

    fun setNavigationBarColor(
        window: Window,
        @ColorRes color: Int
    ) {
        window.navigationBarColor = context.getColor(color)
    }

    fun setOnApplyWindowInsetsTopMarginListener(
        window: Window,
        view: View
    ) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(view) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }

            WindowInsetsCompat.CONSUMED
        }
    }


    /**
     * status bar icon color : black
     * navigation bar icon color : black
     */
    fun setBlackSystemBarIconColor(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    /**
     * status bar icon color : white
     * navigation bar icon color : white
     */
    fun setWhiteSystemBarIconColor(
        window: Window
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        } else {
            window.decorView.systemUiVisibility = 0
        }
    }

    /**
     * status bar icon color : white
     * navigation bar icon color : black
     */
    fun setWhiteStatusBarAndBlackNavigationBarIconColor(
        window: Window
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    /**
     * status bar icon color : black
     * navigation bar icon color : white
     */
    fun setBlackStatusBarAndWhiteNavigationBarIconColor(
        window: Window
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility =  View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    /**
     * 바텀시트 다이어로그 호출 시,
     * 네비게이션 바(navigation bar) 딤 처리 되는 것을 흰색으로 색상변경할 수 있는 함수
     *
     * @RequireAPI : API 29 이하
     */
    fun setWhiteNavigationBar(dialog: Dialog) {
        val window: Window? = dialog.window
        if (window != null) {
            val metrics = DisplayMetrics()
            window.windowManager.defaultDisplay.getMetrics(metrics)

            val dimDrawable = ColorDrawable(Color.TRANSPARENT)
            val navigationBarDrawable = ColorDrawable(Color.WHITE)

            val layers = arrayOf(dimDrawable, navigationBarDrawable)
            val windowBackground = LayerDrawable(layers)
            windowBackground.setLayerInsetTop(1, metrics.heightPixels)

            window.setBackgroundDrawable(windowBackground)
        }
    }
}
