package com.depromeet.presentation.home.customview

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

    private var text: String = ""
    private var textSize: Float = 0f
    private var textColor: Int = 0
    private var backgroundColor: Int = 0
    private var paddingLeftValue: Float = 0f
    private var paddingRightValue: Float = 0f
    private var paddingTopValue: Float = 0f
    private var paddingBottomValue: Float = 0f
    private var triangleHeight = 0f
    private var triangleWidth = 0f
    private var cornerRadius = 0f
    private var textStyle: Int = 0

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CustomSpeechBubbleView, defStyleAttr, 0)
            .apply {
                try {
                    backgroundColor = getColor(
                        R.styleable.CustomSpeechBubbleView_backgroundColor,
                        context.getColor(R.color.black)
                    )
                    textColor = getColor(
                        R.styleable.CustomSpeechBubbleView_textColor,
                        context.getColor(R.color.white)
                    )
                    textSize = getDimension(
                        R.styleable.CustomSpeechBubbleView_textSize,
                        resources.getDimension(R.dimen.speech_bubble_text_size)
                    )
                    text = getString(R.styleable.CustomSpeechBubbleView_text) ?: ""
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
                    textStyle =
                        getInt(R.styleable.CustomSpeechBubbleView_textStyle, Typeface.NORMAL)
                } finally {
                    recycle()
                }
            }

        bubblePaint.color = backgroundColor
        bubblePaint.style = Paint.Style.FILL
        textPaint.color = textColor
        textPaint.textSize = textSize
        textPaint.textAlign = Paint.Align.CENTER

        textPaint.typeface = when (textStyle) {
            1 -> ResourcesCompat.getFont(context, com.depromeet.designsystem.R.font.font_pretendard_medium)
            2 -> ResourcesCompat.getFont(context, com.depromeet.designsystem.R.font.font_pretendard_semibold)
            3 -> ResourcesCompat.getFont(context, com.depromeet.designsystem.R.font.font_pretendard_regular)
            else -> ResourcesCompat.getFont(context, com.depromeet.designsystem.R.font.font_pretendard_regular)
        }
    }


    fun setText(text: String) {
        this.text = text
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val textWidth = textPaint.measureText(text)
        val desiredWidth = (textWidth + paddingBottomValue + paddingRightValue).toInt()
        val desiredHeight =
            (paddingTopValue + textSize + paddingBottomValue + triangleHeight).toInt()

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 둥근 사각형
        val width = width.toFloat()
        val height = height.toFloat()

        val bubbleHeight = height - triangleHeight
        bubbleRect.set(
            0f, 0f, width, bubbleHeight
        )
        canvas.drawRoundRect(bubbleRect, cornerRadius, cornerRadius, bubblePaint)

        // 삼각형
        bubblePath.reset()
        val triangleX = width / 2 - triangleWidth
        val triangleY = height - triangleHeight

        bubblePath.moveTo(triangleX, triangleY)
        bubblePath.lineTo(triangleX + triangleWidth, triangleY + triangleHeight)
        bubblePath.lineTo(triangleX + triangleWidth * 2, triangleY)
        bubblePath.close()

        canvas.drawPath(bubblePath, bubblePaint)

        // 텍스트
        val textX = width / 2
        val textY = bubbleHeight / 2 - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(text, textX, textY, textPaint)
    }
}
