package com.tsai.flowsample.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.AbsSavedState
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.button.MaterialButton

/**
 * This custom button can has loading state to provide progress bar on the button
 * and has lifecycle aware animation
 *
 * @author Small Tsai
 */
class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    companion object {
        const val REPLACEMENT_CHAR = '\uFFFD'
    }

    enum class ProgressStyle {
        Center, Right
    }

    inner class SavedState(superState: Parcelable) : AbsSavedState(superState) {
        var isLoading: Boolean? = null
        var textCache: CharSequence? = null
        var shouldUseMeasuredWidth: Boolean? = null
        var textWidthCache: Int? = null
    }

    private inner class CustomDrawableSpan(drawable: Drawable) : ImageSpan(drawable) {
        override fun getSize(
            paint: Paint,
            text: CharSequence,
            start: Int,
            end: Int,
            fontMetricsInt: Paint.FontMetricsInt?
        ): Int {
            // get drawable dimensions
            val rect = drawable.bounds
            fontMetricsInt?.let {
                val fontMetrics = paint.fontMetricsInt

                // get a font height
                val lineHeight = fontMetrics.bottom - fontMetrics.top

                // make sure our drawable has height >= font height
                val drHeight = lineHeight.coerceAtLeast(rect.bottom - rect.top)

                val centerY = fontMetrics.top + lineHeight / 2

                // adjust font metrics to fit our drawable size
                fontMetricsInt.apply {
                    ascent = centerY - drHeight / 2
                    descent = centerY + drHeight / 2
                    top = ascent
                    bottom = descent
                }
            }

            // return drawable width which is in our case = drawable width + margin from text
            return rect.width() + progressBarMarginStart
        }

        override fun draw(
            canvas: Canvas,
            text: CharSequence,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {

            canvas.save()
            val fontMetrics = paint.fontMetricsInt
            // get font height. in our case now it's drawable height
            val lineHeight = fontMetrics.bottom - fontMetrics.top

            // adjust canvas vertically to draw drawable on text vertical center
            val centerY = y + fontMetrics.bottom - lineHeight / 2
            val transY = centerY - drawable.bounds.height() / 2
            val transX = when (progressStyle) {
                ProgressStyle.Center -> {
                    (textWidthCache
                        ?.div(2)
                        ?.minus(paddingRight)
                        ?.minus(paddingLeft)
                        ?.plus(2.5f) // progress bar stroke width
                        ?.plus(progressBarMarginStart)
                            ) ?: 0f
                }
                ProgressStyle.Right -> {
                    x + progressBarMarginStart
                }
            }

            // adjust canvas horizontally to draw drawable with defined margin from text
            canvas.translate(transX, transY.toFloat())
            drawable.draw(canvas)
            canvas.restore()
        }
    }

    var progressStyle: ProgressStyle = ProgressStyle.Center

    var isLoading: Boolean = false
        set(isLoading) {
            isClickable = isLoading.not()
            if (isLoading) {
                startLoading()
                field = isLoading
            } else {
                field = isLoading
                stopLoading()
            }
        }


    private val progressBarMarginStart: Int
        get() = when (progressStyle) {
            ProgressStyle.Center -> 0
            ProgressStyle.Right -> 20
        }

    private var textCache = text
        set(newText) {
            if (field != newText) {
                shouldUseMeasuredWidth = true
            }
            field = newText
        }
    private var textWidthCache: Int? = null
    private var shouldUseMeasuredWidth = false

    private val progressCallback = object : Drawable.Callback {
        override fun unscheduleDrawable(who: Drawable, what: Runnable) {}
        override fun invalidateDrawable(who: Drawable) {
            postInvalidate()
        }

        override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {}
    }

    private val progressDrawable = CircularProgressDrawable(context).apply {
        setStyle(CircularProgressDrawable.DEFAULT)
        setColorSchemeColors(Color.WHITE)
        val size = (centerRadius + strokeWidth).toInt() * 2
        setBounds(0, 0, size, size)
        callback = progressCallback
    }

    private val drawableSpan = CustomDrawableSpan(progressDrawable)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findViewTreeLifecycleOwner()?.lifecycle?.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        isLoading = false
        super.onDestroy(owner)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (isLoading) {
            textCache = text
        } else {
            super.setText(text, type)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (progressStyle) {
            ProgressStyle.Center -> {
                textWidthCache?.let {
                    if (shouldUseMeasuredWidth.not()) {
                        setMeasuredDimension(it, measuredHeight)
                    } else {
                        setMeasuredDimension(measuredWidth, measuredHeight)
                    }
                    shouldUseMeasuredWidth = false
                }
            }
            ProgressStyle.Right -> {}
        }
        textWidthCache = measuredWidth
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val newState = SavedState(superState).apply {
            this.isLoading = this@ProgressButton.isLoading
            this.textCache = this@ProgressButton.textCache
            this.shouldUseMeasuredWidth = this@ProgressButton.shouldUseMeasuredWidth
            this.textWidthCache = this@ProgressButton.textWidthCache
        }
        return newState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            state.textCache?.let {
                textCache = it
                text = it
            }
            state.isLoading?.let {
                isLoading = it
            }
            state.shouldUseMeasuredWidth?.let {
                shouldUseMeasuredWidth = it
            }
            state.textWidthCache?.let {
                textWidthCache = it
            }
        }
        super.onRestoreInstanceState(state)
    }

    private fun startLoading() {
        if (progressDrawable.isRunning) return
        val spanSource = when (progressStyle) {
            ProgressStyle.Center -> "$REPLACEMENT_CHAR"
            ProgressStyle.Right -> "$textCache$REPLACEMENT_CHAR"
        }

        val spannableString = SpannableString(spanSource).apply {
            setSpan(drawableSpan, length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        text = spannableString
        progressDrawable.start()
    }

    private fun stopLoading() {
        if (progressDrawable.isRunning.not()) return
        progressDrawable.stop()
        clearAnimation()
        super.setText(textCache)
    }
}