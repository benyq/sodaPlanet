package com.benyq.sodaplanet.transaction.widget

import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter

/**
 *
 * @author benyq
 * @date 2022/10/14
 * @email 1520063035@qq.com
 *
 */
object DBComponentTransaction {

    @BindingAdapter(value = ["drawableRes", "bgColor"])
    @JvmStatic
    fun ConsumeView.setImageAndBackground(@DrawableRes resId: Int, @ColorRes bg: Int) {
        setBGColor(bg)
        setDrawable(resId)
    }
}