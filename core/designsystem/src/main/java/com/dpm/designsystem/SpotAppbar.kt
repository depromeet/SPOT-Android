package com.dpm.designsystem

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.View.OnClickListener
import androidx.constraintlayout.widget.ConstraintLayout
import com.depromeet.designsystem.R
import com.depromeet.designsystem.databinding.SpotAppbarBinding
import com.dpm.designsystem.extension.dpToPx

class SpotAppbar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private lateinit var binding: SpotAppbarBinding
    var onNavigationClickListener: OnClickListener? = null
    var onMenuClickListener: OnClickListener? = null

    init {
        initView()
        context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.SpotAppbar, defStyleAttr, defStyleAttr
        ).let { typedArray ->
            with(binding) {
                tvTitle.text = typedArray.getString(R.styleable.SpotAppbar_android_text).toString()
                tvTitle.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(
                        R.styleable.SpotAppbar_android_textSize, 0f
                    )
                )
                tvTitle.setTextAppearance(
                    typedArray.getResourceId(
                        R.styleable.SpotAppbar_titleTextAppearance,
                        0
                    )
                )
                tvTitle.setTextColor(
                    typedArray.getColor(
                        R.styleable.SpotAppbar_android_textColor,
                        0
                    )
                )
                val navIconResId =
                    typedArray.getResourceId(R.styleable.SpotAppbar_navigationIcon, 0)
                if (navIconResId != 0) {
                    ivNav.setBackgroundResource(navIconResId)
                    ivNav.backgroundTintList =
                        typedArray.getColorStateList(R.styleable.SpotAppbar_navigationIconColor)
                    ivNav.layoutParams.width = typedArray.getInt(
                        R.styleable.SpotAppbar_navigationIconSize,
                        R.dimen.default_icon_size
                    ).dpToPx(context)
                    ivNav.layoutParams.height = typedArray.getInt(
                        R.styleable.SpotAppbar_navigationIconSize,
                        R.dimen.default_icon_size
                    ).dpToPx(context)
                } else {
                    ivNav.visibility = View.GONE
                }
                val menuIconResId = typedArray.getResourceId(R.styleable.SpotAppbar_menuIcon, 0)
                if (menuIconResId != 0) {
                    ivMenu.setBackgroundResource(menuIconResId)
                    ivMenu.backgroundTintList =
                        typedArray.getColorStateList(R.styleable.SpotAppbar_menuIconColor)
                    ivMenu.layoutParams.width = typedArray.getInt(
                        R.styleable.SpotAppbar_menuIconSize,
                        R.dimen.default_icon_size
                    ).dpToPx(context)
                    ivMenu.layoutParams.height = typedArray.getInt(
                        R.styleable.SpotAppbar_menuIconSize,
                        R.dimen.default_icon_size
                    ).dpToPx(context)
                } else {
                    ivMenu.visibility = View.GONE
                }
                clSpotAppbar.setBackgroundResource(
                    typedArray.getResourceId(
                        R.styleable.SpotAppbar_android_background,
                        0
                    )
                )
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
            ivNav.setOnClickListener { onNavigationClickListener?.onClick(it) }
            ivMenu.setOnClickListener { onMenuClickListener?.onClick(it) }
        }
    }

    fun setText(text: String) {
        binding.tvTitle.text = text
    }

    inline fun setNavigationOnClickListener(crossinline onClick: (View) -> Unit) {
        this.onNavigationClickListener = OnClickListener { view -> onClick(view) }
    }

    inline fun setMenuOnClickListener(crossinline onClick: (View) -> Unit) {
        this.onMenuClickListener = OnClickListener { view -> onClick(view) }
    }
}