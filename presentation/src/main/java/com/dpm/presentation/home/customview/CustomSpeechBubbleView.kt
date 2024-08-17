package com.dpm.presentation.home.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.depromeet.presentation.R

class CustomSpeechBubbleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val bubblePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bubblePath: Path = Path()
    private val bubbleRect: RectF = RectF()

    private var textParts: MutableList<String> = mutableListOf("", "", "")
    private var textSize: Float = 0f
    private var backgroundColor: Int = 0
    private var paddingLeftValue: Float = 0f
    private var paddingRightValue: Float = 0f
    private var paddingTopValue: Float = 0f
    private var paddingBottomValue: Float = 0f
    private var triangleHeight = 0f
    private var triangleWidth = 0f
    private var cornerRadius = 0f
    private var textStyle: Int = 0
    private var triangleDirection: Int = 0
    private var textAppearance: Int = 0

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CustomSpeechBubbleView, defStyleAttr, 0)
            .apply {
                try {
                    backgroundColor = getColor(
                        R.styleable.CustomSpeechBubbleView_backgroundColor,
                        context.getColor(R.color.black)
                    )
                    textSize = getDimension(
                        R.styleable.CustomSpeechBubbleView_textSize,
                        resources.getDimension(R.dimen.speech_bubble_text_size)
                    )
                    paddingLeftValue = getDimension(
                        R.styleable.CustomSpeechBubbleView_paddingLeft,
                        resources.getDimension(R.dimen.speech_bubble_padding_left)
                    )
                    paddingRightValue = getDimension(
                        R.styleable.CustomSpeechBubbleView_paddingRight,
                        resources.getDimension(R.dimen.speech_bubble_padding_right)
                    )
                    paddingTopValue = getDimension(
                        R.styleable.CustomSpeechBubbleView_paddingTop,
                        resources.getDimension(R.dimen.speech_bubble_padding_top)
                    )
                    paddingBottomValue = getDimension(
                        R.styleable.CustomSpeechBubbleView_paddingBottom,
                        resources.getDimension(R.dimen.speech_bubble_padding_bottom)
                    )
                    triangleHeight = getDimension(
                        R.styleable.CustomSpeechBubbleView_triangleHeight,
                        resources.getDimension(R.dimen.speech_bubble_triangle_height)
                    )
                    triangleWidth = getDimension(
                        R.styleable.CustomSpeechBubbleView_triangleWidth,
                        resources.getDimension(R.dimen.speech_bubble_triangle_width)
                    )
                    cornerRadius = getDimension(
                        R.styleable.CustomSpeechBubbleView_cornerRadius,
                        resources.getDimension(R.dimen.speech_bubble_corner_radius)
                    )
                    textStyle = getInt(R.styleable.CustomSpeechBubbleView_textStyle, Typeface.BOLD)
                    triangleDirection =
                        getInt(R.styleable.CustomSpeechBubbleView_triangleDirection, 0)
                    textAppearance =
                        getResourceId(R.styleable.CustomSpeechBubbleView_textAppearance, 0)


                } finally {
                    recycle()
                }
            }

        bubblePaint.color = backgroundColor
        bubblePaint.style = Paint.Style.FILL
        textPaint.color =
            context.getColor(com.depromeet.designsystem.R.color.color_foreground_body_sebtext)
        textPaint.textAlign = Paint.Align.LEFT

        if (textAppearance != 0) {
            applyTextAppearance(textAppearance)
        } else {
            textPaint.textSize = textSize
            textPaint.typeface = when (textStyle) {
                1 -> ResourcesCompat.getFont(
                    context,
                    com.depromeet.designsystem.R.font.font_pretendard_medium
                )

                2 -> ResourcesCompat.getFont(
                    context,
                    com.depromeet.designsystem.R.font.font_pretendard_semibold
                )

                3 -> ResourcesCompat.getFont(
                    context,
                    com.depromeet.designsystem.R.font.font_pretendard_regular
                )

                else -> ResourcesCompat.getFont(
                    context,
                    com.depromeet.designsystem.R.font.font_pretendard_regular
                )
            }

        }
    }

    @SuppressLint("CustomViewStyleable")
    private fun applyTextAppearance(styleResId: Int) {
        val typedArray =
            context.obtainStyledAttributes(styleResId, R.styleable.CustomTextAppearance)
        try {
            val fontResId =
                typedArray.getResourceId(R.styleable.CustomTextAppearance_android_fontFamily, 0)
            val textSizeFromAppearance =
                typedArray.getDimension(R.styleable.CustomTextAppearance_android_textSize, textSize)

            if (fontResId != 0) {
                textPaint.typeface = ResourcesCompat.getFont(context, fontResId)
            }
            textPaint.textSize = textSizeFromAppearance
        } finally {
            typedArray.recycle()
        }
    }

    fun setText(text: String) {
        textParts[0] = text
        textParts[1] = ""
        textParts[2] = ""
        requestLayout()
        invalidate()
    }

    fun setTextPart(prefix: String?, number: Int?, suffix: String?) {
        textParts[0] = prefix ?: ""
        textParts[1] = number?.toString() ?: ""
        textParts[2] = suffix ?: ""
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val totalTextWidth = textParts.fold(0f) { acc, textPart ->
            acc + textPaint.measureText(textPart)
        }
        val desiredWidth = (totalTextWidth + paddingLeftValue + paddingRightValue).toInt()
        val desiredHeight =
            (paddingTopValue + textSize + paddingBottomValue + triangleHeight).toInt()

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        var bubbleHeight = height - triangleHeight

        bubblePath.reset()

        when (triangleDirection) {
            0 -> {
                bubbleHeight = height - triangleHeight
                bubbleRect.set(0f, 0f, width, bubbleHeight)
                canvas.drawRoundRect(bubbleRect, cornerRadius, cornerRadius, bubblePaint)

                val triangleX = width / 2 - triangleWidth / 2
                val triangleY = height - triangleHeight

                bubblePath.moveTo(triangleX, triangleY)
                bubblePath.lineTo(triangleX + triangleWidth, triangleY)
                bubblePath.lineTo(width / 2, height)
                bubblePath.close()
            }

            1 -> {
                bubbleHeight = height - triangleHeight
                bubbleRect.set(0f, triangleHeight, width, bubbleHeight)
                canvas.drawRoundRect(bubbleRect, cornerRadius, cornerRadius, bubblePaint)

                val triangleX = width / 2 - triangleWidth / 2
                val triangleY = 0f

                bubblePath.moveTo(triangleX, triangleY + triangleHeight)
                bubblePath.lineTo(triangleX + triangleWidth, triangleY + triangleHeight)
                bubblePath.lineTo(width / 2, triangleY)
                bubblePath.close()
            }
        }

        canvas.drawPath(bubblePath, bubblePaint)

        // 텍스트 그리기
        var xOffset = paddingLeftValue
        textParts.forEachIndexed { index, textPart ->
            textPaint.color = when (index) {
                1 -> context.getColor(com.depromeet.designsystem.R.color.color_action_enabled)
                else -> context.getColor(com.depromeet.designsystem.R.color.color_foreground_body_sebtext)
            }

            val textWidth = textPaint.measureText(textPart)
            val textX = xOffset
            val textY = bubbleHeight / 2 - (textPaint.descent() + textPaint.ascent()) / 2

            canvas.drawText(textPart, textX, textY, textPaint)
            xOffset += textWidth
        }
    }
}

