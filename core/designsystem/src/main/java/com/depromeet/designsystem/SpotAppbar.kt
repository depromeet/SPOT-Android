package com.depromeet.designsystem

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.View.OnClickListener
import androidx.constraintlayout.widget.ConstraintLayout
import com.depromeet.designsystem.databinding.SpotAppbarBinding

class SpotAppbar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
): ConstraintLayout(context, attributeSet, defStyleAttr) {
    private lateinit var binding: SpotAppbarBinding
    var onNavigationClickListener: OnClickListener? = null

    init {
        initView()
        context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.SpotAppbar, defStyleAttr, defStyleAttr
        ).let { typedArray ->
            with(binding) {
                tvTitle.text = typedArray.getString(R.styleable.SpotAppbar_android_text).toString()
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(
                    R.styleable.SpotAppbar_android_textSize, 0f
                ))
                tvTitle.setTextColor(typedArray.getColor(R.styleable.SpotAppbar_android_textColor, 0))
                ivNav.setBackgroundResource(typedArray.getResourceId(R.styleable.SpotAppbar_navigationIcon, 0))
            }
            typedArray.recycle()
        }
    }

    private fun initView() {
        binding = SpotAppbarBinding.bind(
            inflate(
                context,
                R.layout.spot_appbar,
                this
            )
        ).apply {
            ivNav.setOnClickListener { onNavigationClickListener?.onClick(it)}
        }
    }

    inline fun setNavigationOnClickListener(crossinline onClick: (View) -> Unit) {
        this.onNavigationClickListener = OnClickListener { view -> onClick(view) }
    }
}