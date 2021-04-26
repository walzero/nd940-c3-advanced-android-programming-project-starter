package com.udacity

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
import java.util.concurrent.TimeUnit

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val PROGRESS = "progress"
    }

    private lateinit var currentRect: Rect
    private lateinit var arcBounds: RectF
    private var progress: Float = 1f
    private var widthSize = 0
    private var heightSize = 0

    @ColorRes
    private var backgroundColorStateList: Int = 0

    @ColorRes
    private var textColorStateList: Int = 0

    private var isActive: Boolean = false
        set(value) {
//            isClickable = value
//            isFocusable = value
            field = value
            invalidate()
        }

    fun setButtonActiveState(active: Boolean) {
        isActive = active
    }

    init {
        val customAttrs = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            defStyleAttr,
            0
        )

        try {
            isActive = customAttrs.getBoolean(R.styleable.LoadingButton_active, true)

            val backgroundColor = R.styleable.LoadingButton_btnBackgroundColor
            if (customAttrs.hasValue(backgroundColor)) {
                backgroundColorStateList = customAttrs.getResourceId(backgroundColor, 0)
            }

            val textColor = R.styleable.LoadingButton_btnTextColor
            if (customAttrs.hasValue(textColor)) {
                textColorStateList = customAttrs.getResourceId(textColor, 0)
            }
        } finally {
            customAttrs.recycle()
        }
    }

    private val widthProperty by lazy {
        PropertyValuesHolder.ofFloat(PROGRESS, 0f, 1f)
    }

    private val valueAnimator by lazy {
        ValueAnimator().apply { interpolator = DecelerateInterpolator(1f) }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = context.resources.getDimension(R.dimen.default_text_size)
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
        isClickable = true
    }

    override fun performClick(): Boolean {
        if (isActive)
            performAnimations()

        return super.performClick()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val xPos: Float = (width / 2).toFloat()
        val yPos: Float = (height / 2 - (paint.descent() + paint.ascent()) / 2)

        when (isActive) {
            false -> drawInactiveButton(canvas, xPos, yPos)
            true -> drawActiveButton(canvas, xPos, yPos)
        }
    }

    private fun drawInactiveButton(
        canvas: Canvas,
        xPos: Float,
        yPos: Float
    ) {
        canvas.drawColor(Color.LTGRAY)

        //Draw the text
        paint.color = Color.WHITE
        canvas.drawText(
            context.getString(R.string.button_name),
            xPos,
            yPos,
            paint
        )
    }

    private fun drawActiveButton(
        canvas: Canvas,
        xPos: Float,
        yPos: Float
    ) {
        //Draw the Background
        canvas.drawColor(Color.DKGRAY)

        //Draw the progress
        paint.color = context.getColor(backgroundColorStateList)
        canvas.drawRect(currentRect, paint)

        //Draw the text
        paint.color = context.getColor(textColorStateList)
        canvas.drawText(
            context.getString(R.string.button_name),
            xPos,
            yPos,
            paint
        )

        //Draw the Arc
        paint.color = Color.WHITE
        canvas.drawArc(arcBounds, 0f, calculateArcSweepValue(), true, paint)
    }

    private fun calculateArcSweepValue() = if (progress == 1f) 0f else (360f * progress)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
        updateRects()
    }


    private fun performAnimations() {
        valueAnimator.apply {
            removeAllUpdateListeners()
            setValues(widthProperty)
            duration = TimeUnit.SECONDS.toMillis(1)
            addUpdateListener {
                progress = it.getAnimatedValue(PROGRESS) as Float
                updateRects()
                invalidate()
            }
            doOnEnd {
                isEnabled = true
            }
        }.start().also { isEnabled = false }
    }

    private fun updateRects() {
        currentRect = Rect(
            0, 0, (widthSize * progress).toInt(), heightSize
        )

        //bounds summary:
        //width = width - margin - size, width - margin
        //height = half height - half size, half height + half size
        arcBounds = RectF(
            widthSize - 48.inPixels,
            ((heightSize * getArcHeightFactor()) / 2) - getArcHalfSizeValue(),
            widthSize - 16.inPixels,
            ((heightSize * getArcHeightFactor()) / 2) + getArcHalfSizeValue()
        )
    }

    /**
     * If progress more then 30%:
     *show arc, consider size in height
     * If progress less then 30%:
     *hide arc above the button, consider size in height
     */
    private fun getArcHalfSizeValue() =
        if (progress > 0.3f) 16.inPixels else (-16).inPixels

    /**
     * if progress more then 30% show arc, else hide above button
     */
    private fun getArcHeightFactor() = if (progress > 0.3f) progress else 0f

    private val Int.inPixels: Float
        get() = this * Resources.getSystem().displayMetrics.density
}