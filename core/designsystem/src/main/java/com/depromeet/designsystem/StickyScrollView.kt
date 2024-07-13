package com.depromeet.designsystem

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ScrollView

class StickyScrollView : ScrollView, ViewTreeObserver.OnGlobalLayoutListener {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
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
            field?.let {
                it.translationZ = 1f //천장 뷰가 다른 뷰를 가리지 않게
                it.setOnClickListener { _ ->
                    //클릭 시, 헤더뷰가 최상단으로 오게 스크롤 이동
                    this.smoothScrollTo(scrollX, it.top)
                    callStickListener()
                }
            }
        }

    override fun onGlobalLayout() {
        mHeaderInitPosition = header?.top?.toFloat() ?: 0f
    }

    override fun onScrollChanged(l: Int, scrollY: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, scrollY, oldl, oldt)

        if (scrollY > mHeaderInitPosition) {
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