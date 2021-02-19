package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = context.resources.getDimension(R.dimen.default_text_size)
        typeface = Typeface.create("", Typeface.BOLD)
        color = Color.WHITE
    }

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

    }


    init {

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val xPos: Float = (width / 2).toFloat()
        val yPos: Float = (height / 2 - (paint.descent() + paint.ascent()) / 2)
        canvas.drawColor(context.getColor(R.color.colorPrimary))
        canvas.drawText(
            context.getString(R.string.button_name),
            xPos,
            yPos,
            paint
        )
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
    }

}