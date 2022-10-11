package com.benyq.sodaplanet.base.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import com.benyq.sodaplanet.base.R
import com.benyq.sodaplanet.base.ext.dp

/**
 *
 * @author benyq
 * @date 2022/10/10
 * @email 1520063035@qq.com
 *
 */
class BottomTabTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var drawableWidth = 0
    private var drawableHeight = 0

    private var left: Drawable? = null
    private var top: Drawable? = null
    private var right: Drawable? = null
    private var bottom: Drawable? = null

    init {
        val array = getContext().obtainStyledAttributes(attrs, R.styleable.BottomTabTextView)
        val defaultSize = 30.dp
        drawableWidth = array.getDimension(R.styleable.BottomTabTextView_drawable_width, defaultSize).toInt()
        drawableHeight = array.getDimension(R.styleable.BottomTabTextView_drawable_height, defaultSize).toInt()
        array.recycle()
        setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
    }

    override fun setCompoundDrawables(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
        top?.setBounds(0, 0, drawableWidth, drawableHeight)
        super.setCompoundDrawables(left, top, right, bottom)
    }


    override fun setCompoundDrawablesWithIntrinsicBounds(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
    }
}