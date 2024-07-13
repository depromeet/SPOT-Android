package com.depromeet.presentation.home.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.depromeet.presentation.R

class CustomSpeechBubbleView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val bubblePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bubblePath: Path = Path()
    private val bubbleRect: RectF = RectF()
    private val typedArray =
        context.obtainStyledAttributes(attrs, R.styleable.CustomSpeechBubbleView)
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

    init {
        backgroundColor = typedArray.getColor(
            R.styleable.CustomSpeechBubbleView_backgroundColor,
            context.getColor(R.color.black)
        )
        textColor = typedArray.getColor(
            R.styleable.CustomSpeechBubbleView_textColor,
            context.getColor(R.color.white)
        )
        textSize = typedArray.getDimension(
            R.styleable.CustomSpeechBubbleView_textSize,
            resources.getDimension(R.dimen.speech_bubble_text_size)
        )
        text = typedArray.getString(R.styleable.CustomSpeechBubbleView_text) ?: ""
        paddingLeftValue = typedArray.getDimension(
            R.styleable.CustomSpeechBubbleView_paddingLeft,
            resources.getDimension(R.dimen.speech_bubble_padding_left)
        )
        paddingRightValue = typedArray.getDimension(
            R.styleable.CustomSpeechBubbleView_paddingRight,
            resources.getDimension(R.dimen.speech_bubble_padding_right)
        )
        paddingTopValue = typedArray.getDimension(
            R.styleable.CustomSpeechBubbleView_paddingTop,
            resources.getDimension(R.dimen.speech_bubble_padding_top)
        )
        paddingBottomValue = typedArray.getDimension(
            R.styleable.CustomSpeechBubbleView_paddingBottom,
            resources.getDimension(R.dimen.speech_bubble_padding_bottom)
        )
        triangleHeight = typedArray.getDimension(
            R.styleable.CustomSpeechBubbleView_triangleHeight,
            resources.getDimension(R.dimen.speech_bubble_triangle_height)
        )
        triangleWidth = typedArray.getDimension(
            R.styleable.CustomSpeechBubbleView_triangleWidth,
            resources.getDimension(R.dimen.speech_bubble_triangle_width)
        )
        cornerRadius = typedArray.getDimension(
            R.styleable.CustomSpeechBubbleView_cornerRadius,
            resources.getDimension(R.dimen.speech_bubble_corner_radius)
        )
        typedArray.recycle()

        bubblePaint.color = backgroundColor
        bubblePaint.style = Paint.Style.FILL
        textPaint.color = textColor
        textPaint.textSize = textSize
        textPaint.textAlign = Paint.Align.CENTER
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