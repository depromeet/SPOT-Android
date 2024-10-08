package com.dpm.designsystem

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.widget.NestedScrollView

class StickyNestedScrollView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : NestedScrollView(context, attr, defStyleAttr), ViewTreeObserver.OnGlobalLayoutListener {


    init {
        overScrollMode = OVER_SCROLL_NEVER
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    var stickListener: (View) -> Unit = {}
    var freeListener: (View) -> Unit = {}
    private var mIsHeaderSticky = false
    private var mHeaderInitPosition = 0f

    var header: View? = null
        set(value) {
            field = value
            field?.let { newHeader ->
                newHeader.translationZ = 1f // 천장 뷰가 다른 뷰 가리지 않게
                newHeader.setOnClickListener { _ ->
                    //smoothScrollTo(scrollX, newHeader.top)
                    callStickListener()
                }
                mHeaderInitPosition = newHeader.top.toFloat()
            } ?: kotlin.run {
                mHeaderInitPosition = 0f
            }
        }

    override fun onGlobalLayout() {
        mHeaderInitPosition = header?.top?.toFloat() ?: 0f
    }

    override fun onScrollChanged(l: Int, scrollY: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, scrollY, oldl, oldt)

        if (header != null && scrollY > mHeaderInitPosition) {
            stickHeader(scrollY - mHeaderInitPosition)
        } else {
            freeHeader()
        }
    }

    private fun stickHeader(position: Float) {
        header?.translationY = position
        callStickListener()
    }

    private fun callStickListener() {
        if (!mIsHeaderSticky) {
            stickListener(header ?: return)
            mIsHeaderSticky = true
        }
    }

    private fun freeHeader() {
        header?.translationY = 0f
        callFreeListener()
    }

    private fun callFreeListener() {
        if (mIsHeaderSticky) {
            freeListener(header ?: return)
            mIsHeaderSticky = false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

}