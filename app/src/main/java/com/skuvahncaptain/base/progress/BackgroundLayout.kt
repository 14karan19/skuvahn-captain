package com.skuvahncaptain.base.progress

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import com.skuvahncaptain.R

internal class BackgroundLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mCornerRadius = 0f
    private var mBackgroundColor = 0

    init {
        val color = context.getColor(R.color.black)
        mBackgroundColor = color
        initBackground(color, mCornerRadius)
    }

    private fun initBackground(color: Int, cornerRadius: Float) {
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(color)
            this.cornerRadius = cornerRadius
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = drawable
        } else {
            @Suppress("DEPRECATION")
            setBackgroundDrawable(drawable)
        }
    }

    fun setCornerRadius(radius: Float) {
        mCornerRadius = Helper.dpToPixel(radius, context).toFloat()
        initBackground(mBackgroundColor, mCornerRadius)
    }

    fun setBaseColor(color: Int) {
        mBackgroundColor = color
        initBackground(mBackgroundColor, mCornerRadius)
    }
}
