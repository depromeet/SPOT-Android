package com.depromeet.core.base

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.depromeet.core.extension.hideKeyboard

abstract class BaseActivity<B : ViewBinding>(
    val bindingFactory: (LayoutInflater) -> B,
) : AppCompatActivity() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            val currentFocus = currentFocus
            if (currentFocus != null && isTouchOutsideView(currentFocus, ev)) {
                hideKeyboard(currentFocus)
                currentFocus.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isTouchOutsideView(view: View, ev: MotionEvent): Boolean {
        val outRect = Rect(view.left, view.top, view.right, view.bottom)
        return !outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())
    }
}
