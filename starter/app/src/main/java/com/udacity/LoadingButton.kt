package com.udacity

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val PROGRESS = "progress"
    }

    private var progress: Float = 0f
    private lateinit var currentRect: Rect

    private val widthProperty by lazy {
        PropertyValuesHolder.ofFloat(PROGRESS, 0f, 1f)
    }

    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = context.resources.getDimension(R.dimen.default_text_size)
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

    }

    init {
        isClickable = true
    }

    override fun performClick(): Boolean {
        performAnimations()

        return super.performClick()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val xPos: Float = (width / 2).toFloat()
        val yPos: Float = (height / 2 - (paint.descent() + paint.ascent()) / 2)
        //Draw the Background
        canvas.drawColor(Color.DKGRAY)

        //Draw the progress
        paint.color = context.getColor(R.color.colorPrimary)
        canvas.drawRect(currentRect, paint)

        //Draw the text
        paint.color = Color.WHITE
        canvas.drawText(
            context.getString(R.string.button_name),
            xPos,
            yPos,
            paint
        )

        //Draw the Arc
        canvas.drawArc()
    }

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
        updateRect()
    }


    private fun performAnimations() {
        valueAnimator.apply {
            removeAllUpdateListeners()
            setValues(widthProperty)
            duration = TimeUnit.SECONDS.toMillis(2)
            addUpdateListener {
                progress = it.getAnimatedValue(PROGRESS) as Float
                updateRect()
                updateArc()
                invalidate()
            }
            doOnEnd { isEnabled = true }
        }.start().also { isEnabled = false }
    }

    private fun updateRect() {
        currentRect = Rect(
            0, 0, (widthSize * progress).toInt(), heightSize
        )
    }

    private fun updateArc() {

    }
}