package com.depromeet.presentation.home

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
    private val bubblePath: Path = Path()
    private val bubbleRect: RectF = RectF()
    private var text: String = ""
    private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textSize: Float = 0f
    private var textColor: Int = 0
    private var backgroundColor: Int = 0
    private val typedArray =
        context.obtainStyledAttributes(attrs, R.styleable.CustomSpeechBubbleView)
    private val paddingLeft = resources.getDimension(R.dimen.speech_bubble_padding_left)
    private val paddingRight = resources.getDimension(R.dimen.speech_bubble_padding_right)
    private val paddingTop = resources.getDimension(R.dimen.speech_bubble_padding_top)
    private val paddingBottom = resources.getDimension(R.dimen.speech_bubble_padding_bottom)
    private val triangleHeight = resources.getDimension(R.dimen.speech_bubble_triangle_height)
    private val triangleWidth = 20f

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
        val desiredWidth = (textWidth + paddingLeft + paddingRight).toInt()
        val desiredHeight = (paddingTop + textSize + paddingBottom + triangleHeight).toInt()

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
        val cornerRadius = resources.getDimension(R.dimen.speech_bubble_corner_radius)
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
